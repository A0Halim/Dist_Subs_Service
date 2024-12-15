require_relative 'message_pb'
require_relative 'configuration_pb'
require_relative 'capacity_pb'
require 'socket'

CONF = "dist_subs.conf"
SERVER_HOST = "localhost"
JAVA_SERVER_PORTS = [5001]#, 5002, 5003]
PYTHON_SERVER_PORT = 5004

def read_conf(conf)
  # dist_subs.conf dosyasından fault_tolerance_level okuma işlemi
  fault_tolerance_level = 0
  File.open(conf, 'r') do |file|
    file.each_line do |line|
      fault_tolerance_level = line.split('=', 2).last.to_i
    end
  end
  fault_tolerance_level
end

def connect_to_servers(ports)
  connections = []

  ports.each do |port|
    begin
      socket = TCPSocket.new(SERVER_HOST, port)
      puts "Sunucu bağlantısı kuruldu. port: #{port}"
      connections << socket
    rescue Errno::ECONNREFUSED
      puts "#{port} portu ile bağlantı kurulamadı."
    end
  end

  connections
end

def send_config(socket, fault_tolerance_level)
  # Configuration nesnesini Java sunucusuna gönder
  config = Configuration.new(fault_tolerance_level: fault_tolerance_level, method: MethodType::STRT)
  config_proto = config.to_proto
  socket.write([config_proto.bytesize].pack("N") + config_proto)
  puts "Config gönderildi"

  # Java sunucusundan yanıt mesajını al
  response_length = socket.read(4).unpack("N")[0]
  response_proto = socket.read(response_length)
  response = Message.decode(response_proto)
  puts "Sunucudan yanıt alındı: #{response.demand}, #{response.response}"

  response
end

def request_capacity(socket)
  # Java sunucusuna Capacity talebi gönder
  request = Message.new(demand: Demand::CPCTY, response: Response::NULL)
  request_proto = request.to_proto
  socket.write([request_proto.bytesize].pack("N") + request_proto)
  puts "Capacity talebi gönderildi"

  # Java sunucusundan Capacity yanıtını al
  response_length = socket.read(4).unpack("N")[0]
  response_proto = socket.read(response_length)
  response = Capacity.decode(response_proto)
  puts "Capacity alındı: #{response.serverX_status}, #{response.timestamp}"

  response
end

def python_handler(socket, capacity)
  # Capacity nesnesini Python sunucusuna gönder
  capacity_proto = capacity.to_proto
  socket.write([capacity_proto.size].pack("N") + capacity_proto)
  puts "Python sunucusuna capacity nesnesi gönderildi: #{capacity.serverX_status}, #{capacity.timestamp}"
end

def server_connection_handler(java_connections, python_connection, fault_tolerance_level)
  loop do
    java_connections.each_with_index do |connection, index|
      begin
        puts "Java sunucu #{index + 1} çalışıyor."
        response = send_config(connection, fault_tolerance_level)

        if (response.response.to_s == "YEP")
          capacity = request_capacity(connection)

          if capacity
            python_handler(python_connection, capacity)
          else
            puts "Java sunucu #{index + 1} kapasite bilgisi alınamadı."
          end

        else
          puts "Java sunucusundan gerekli izinler alınamadı. Lütfen tekrar deneyin."
        end

        rescue IOError
          puts "Java sunucu #{index + 1} ile bağlantı kesildi."
      end
    end
    sleep(5)
  end
end

fault_tolerance_level = read_conf(CONF)
java_connections = connect_to_servers(JAVA_SERVER_PORTS)
python_connection = connect_to_servers([PYTHON_SERVER_PORT]).first

server_connection_handler(java_connections, python_connection, fault_tolerance_level)
