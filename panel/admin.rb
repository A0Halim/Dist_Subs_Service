require_relative 'message_pb'
require_relative 'configuration_pb'
require_relative 'capacity_pb'
require 'socket'

CONF = "dist_subs.conf"
SERVER_PORT1 = 5001
SERVER_PORT2 = 5002
SERVER_PORT3 = 5003
PYTHON_PORT = 5004

def start(jserver1, jserver2, jserver3, pserver)
  fault_tolerance_level = read_conf(CONF)
  jconn1 = jserver1.accept
  puts "Sunucu 1 ile bağlantı kuruldu"
  jconn2 = jserver2.accept
  puts "Sunucu 2 ile bağlantı kuruldu"
  jconn3 = jserver3.accept
  puts "Sunucu 3 ile bağlantı kuruldu"
  pconn = pserver.accept
  puts "Python sunucusu ile bağlantı kuruldu"
  i = 1

  loop do
    handle_jserver(jconn1, fault_tolerance_level, pconn, i)
    handle_jserver(jconn2, fault_tolerance_level, pconn, i)
    handle_jserver(jconn3, fault_tolerance_level, pconn, i)

    i += 1
    sleep(5)
  end
end

def read_conf(conf)
  #dist_subs.conf dosyası okundu
  value = 0
  File.open(CONF, 'r') do |file|
    file.each_line do |line|
      value = line.split('=', 2).last.to_i
    end
  end
  value
end

def python_handler(pconn, cpcty)
  #Python'a capacity nesnesi gönderimi
  cpctyPlotter = Capacity.new(serverX_status: cpcty.serverX_status, timestamp: cpcty.timestamp)
  plotterData = cpctyPlotter.to_proto
  pconn.write([plotterData.size].pack("N") + plotterData)
  puts "Pythona capacity nesnesi gönderildi"
end

def handle_jserver(conn, fault_tolerance_level, pconn, i)

  if(i == 1)
  puts "İstemci ile bağlanti kuruldu."
  #Config dosyasından okunan veriyi java serverına yollama
  response = Configuration.new(fault_tolerance_level: fault_tolerance_level, method_type: MethodType::STRT)
  data = response.to_proto
  conn.write([data.size].pack("N") + data)
  puts "Config gönderildi"
  
  #Java serverından gelen başarılı başarısız message nesnesi
  length = conn.read(4).unpack("N")[0]
  rmessage = conn.read(length)
  message = Message.decode(rmessage)
  puts "Başarili başarisiz message nesnesi alindi: #{message.demand}, #{message.response}"
  end

  #Java serverından capacity nesnesi talebi
  cpctyInquiry = Message.new(demand: Demand::CPCTY, response: Response::NULL)
  cpctyData = cpctyInquiry.to_proto
  conn.write([cpctyData.size].pack("N") + cpctyData)
  puts "Java serverindan capacity nesnesi istendi"

  #Java serverından gelen capacity nesnesi
  capacityLength = conn.read(4).unpack("N")[0]
  receivedCapacity = conn.read(capacityLength)
  receivedCpcty = Capacity.decode(receivedCapacity)
  puts "Java serverinden gelen capacity nesnesi alindi: #{receivedCpcty.serverX_status}, #{receivedCpcty.timestamp} #{i}. tekrar"

  python_handler(pconn, receivedCpcty)
end

  jserver1 = TCPServer.new(SERVER_PORT1)
  jserver2 = TCPServer.new(SERVER_PORT2)
  jserver3 = TCPServer.new(SERVER_PORT3)
  pserver = TCPServer.new(PYTHON_PORT)
  start(jserver1, jserver2, jserver3, pserver)