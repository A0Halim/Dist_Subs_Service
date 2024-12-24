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

def connect_to_servers(ports)
  connections = []

  ports.each do |port|
    begin
      socket = TCPSocket.new(SERVER_HOST, port)
      puts "Sunucu bağlantısı kuruldu port: #{port}"
      connections << socket
    rescue Errno::ECONNREFUSED
      puts "#{port} portu ile bağlantı kurulamadı."
    rescue => e
      puts "Sunucu bağlantısı sırasında bir hata oluştu: #{e.message}"
    end
  end

  connections
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

# Capacity talebi gönderme ve yanıt alma

def request_capacity(socket)
  begin
    request = Message.new(demand: Demand::CPCTY)
    request_proto = request.to_proto
    socket.write([request_proto.size].pack("N") + request_proto)
    puts "Capacity talebi gönderildi"

    response_length = socket.read(4).unpack("N")[0]
    response_proto = socket.read(response_length)
    response = Capacity.decode(response_proto)
    puts "Capacity alındı: #{response.serverX_status}, #{response.timestamp}"

    response
  rescue IOError
    puts "Server ile bağlantıda hata oluştu."
  rescue => e
    puts "Capacity talebi sırasında bir hata oluştu: #{e.message}"
  end
end

# Python sunucusuna kapasite gönderme

def python_handler(socket, capacity)
  begin
    capacity_proto = capacity.to_proto
    socket.write([capacity_proto.size].pack("N") + capacity_proto)
    puts "Python sunucusuna capacity nesnesi gönderildi: #{capacity.serverX_status}, #{capacity.timestamp}"
  rescue IOError
    puts "Python sunucusuyla bağlantı hatası oluştu."
  rescue => e
    puts "Python sunucusuna kapasite gönderimi sırasında bir hata oluştu: #{e.message}"
  end
end

# Sunucu istek işlemleri

def server_request_handler(java_connections, python_connection, fault_tolerance_level)
  responses = []

  java_connections.each do |connection|
    response = send_config(connection, fault_tolerance_level)
    responses << response if response
  end

  loop do
    java_connections.each_with_index do |connection, index|
      begin

        if responses[index]&.response.to_s == "YEP"
          capacity = request_capacity(connection)
          
          puts "Java sunucu #{index + 1} çalışıyor."
          if capacity
            python_handler(python_connection, capacity)
          else
            puts "Java sunucu #{index + 1}'den kapasite bilgisi alınamadı."
          end

        else
          puts "Java sunucusundan gerekli izinler alınamadı. Lütfen tekrar deneyin."
        end

      rescue IOError
        puts "Java sunucu #{index + 1} ile bağlantı kesildi."
      rescue => e
        puts "Sunucu #{index + 1} işlem sırasında bir hata oluştu: #{e.message}"
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
        python_connection = connect_to_servers([PYTHON_SERVER_PORT]).first
        java_connections = connect_to_servers(JAVA_SERVER_PORTS)

        if java_connections.any? && python_connection
          server_request_handler(java_connections, python_connection, fault_tolerance_level)
          break
        end
      end
    end
  end
end

fault_tolerance_level = read_conf(CONF)
python_connection = connect_to_servers([PYTHON_SERVER_PORT]).first
java_connections = connect_to_servers(JAVA_SERVER_PORTS)

connection_handler(java_connections, python_connection, fault_tolerance_level)