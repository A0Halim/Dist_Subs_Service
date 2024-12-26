require_relative 'message_pb'
require_relative 'configuration_pb'
require_relative 'capacity_pb'
require 'socket'

CONF = "dist_subs.conf"
SERVER_HOST = "localhost"
JAVA_SERVER_PORTS = [5001, 5002, 5003]
PYTHON_SERVER_PORT = 5004

# dist_subs.conf dosyasını okuma

def read_conf(conf)
  fault_tolerance_level = 0
  begin
    File.open(conf, 'r') do |file|
      file.each_line do |line|
        fault_tolerance_level = line.split('=', 2).last.to_i
      end
    end
  rescue Errno::ENOENT
    puts "Config dosyası bulunamadı."
  rescue => e
    puts "Config dosyası okunurken bir hata oluştu: #{e.message}"
  end
  fault_tolerance_level
end

# Sunucu bağlantıları

def connect_to_java_servers
  connections = {}

  JAVA_SERVER_PORTS.each_with_index do |port, index|
    server_id = index + 1 
    begin
      socket = TCPSocket.new(SERVER_HOST, port)
      puts "Java sunucu #{server_id} bağlantısı kuruldu port: #{port}"
      connections[server_id] = socket
    rescue Errno::ECONNREFUSED
      puts "Server #{server_id} (port: #{port}) ile bağlantı kurulamadı."
    rescue => e
      puts "Server #{server_id} bağlantısında hata: #{e.message}"
    end
  end

  connections
end

def connect_to_python
  begin
    socket = TCPSocket.new(SERVER_HOST, PYTHON_SERVER_PORT)
    puts "Python sunucusu bağlantısı kuruldu port: #{PYTHON_SERVER_PORT}"
    return socket
  rescue Errno::ECONNREFUSED
    puts "Python sunucusu (#{PYTHON_SERVER_PORT}) ile bağlantı kurulamadı."
  rescue => e
    puts "Python sunucusu bağlantısında hata: #{e.message}"
  end
  nil
end

# Config gönderme ve yanıt alma

def send_config(socket, fault_tolerance_level)
  begin
    config = Configuration.new(fault_tolerance_level: fault_tolerance_level, method: MethodType::STRT)
    config_proto = config.to_proto
    socket.write([config_proto.size].pack("N") + config_proto)
    puts "Config gönderildi"

    response_length = socket.read(4).unpack("N")[0]
    response_proto = socket.read(response_length)
    response = Message.decode(response_proto)
    puts "Sunucudan yanıt alındı: #{response.demand}, #{response.response}"

    response
  rescue IOError
    puts "Server ile bağlantıda hata oluştu."
  rescue => e
    puts "Config gönderimi sırasında bir hata oluştu: #{e.message}"
  end
end

# Server_id ataması, capacity talebi gönderme ve yanıt alma

def request_capacity(socket, server_id)
  begin
    request = Message.new(demand: Demand::CPCTY)
    request_proto = request.to_proto
    socket.write([request_proto.size].pack("N") + request_proto)
    puts "Capacity talebi gönderildi"

    cpcty = Capacity.new(server_id: server_id, timestamp: Time.now.to_i)
    cpctyData = cpcty.to_proto
    socket.write([cpctyData.size].pack("N") + cpctyData)
    puts "Gönderildi" 

    response_length = socket.read(4).unpack("N")[0]
    response_proto = socket.read(response_length)
    response = Capacity.decode(response_proto)
    puts "Capacity alındı: Server#{response.server_id}, #{response.serverX_status}, #{response.timestamp}"

    response
  rescue IOError
    puts "Server ile bağlantıda hata oluştu."
  rescue => e
    puts "Capacity talebi sırasında bir hata oluştu: #{e.message}"
  end
end

# Python sunucusuna kapasite gönderme

def python_handler(socket, capacity, server_id)
  begin
    new_capacity = Capacity.new(
      server_id: server_id,
      serverX_status: capacity.serverX_status,
      timestamp: capacity.timestamp
    )
    capacity_proto = new_capacity.to_proto
    socket.write([capacity_proto.size].pack("N") + capacity_proto)
    puts "Python sunucusuna capacity nesnesi gönderildi: Server#{server_id}"
  rescue IOError
    puts "Python sunucusuyla bağlantı hatası oluştu."
  rescue => e
    puts "Python sunucusuna kapasite gönderimi sırasında bir hata oluştu: #{e.message}"
  end
end

# Sunucu istek işlemleri

def server_request_handler(java_connections, python_connection, fault_tolerance_level)
  responses = []

  java_connections.each_value do |connection|
    response = send_config(connection, fault_tolerance_level)
    responses << response if response
  end

  loop do
    java_connections.each_with_index do |(server_id, connection), index|

      begin
        puts "Java sunucu #{server_id} çalışıyor."

        if responses[index]&.response.to_s == "YEP"
          capacity = request_capacity(connection, server_id)

          if capacity
            python_handler(python_connection, capacity, server_id)
          else
            puts "Java sunucu #{server_id}'den kapasite bilgisi alınamadı."
          end

        else
          puts "Java sunucusundan gerekli izinler alınamadı. Lütfen tekrar deneyin."
        end

      rescue IOError
        puts "Java sunucu #{server_id} ile bağlantı kesildi."
      rescue => e
        puts "Sunucuda işlem sırasında bir hata oluştu: #{e.message}"
      end
    end
    sleep(5)
  end
end

def connection_handler(java_connections, python_connection, fault_tolerance_level)
  loop do
    if java_connections.any? && python_connection
      server_request_handler(java_connections, python_connection, fault_tolerance_level)
    else
      puts "Sunucu bağlantısı kurulamadı, tekrar deneniyor..."
      sleep(5)

      loop do
        python_connection = connect_to_python()
        java_connections = connect_to_java_servers()

        if java_connections.any? && python_connection
          server_request_handler(java_connections, python_connection, fault_tolerance_level)
          break
        end
      end
    end
  end
end

fault_tolerance_level = read_conf(CONF)
python_connection = connect_to_python()
java_connections = connect_to_java_servers()

connection_handler(java_connections, python_connection, fault_tolerance_level)