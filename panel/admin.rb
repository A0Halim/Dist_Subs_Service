require_relative 'message_pb'
require_relative 'configuration_pb'
require_relative 'capacity_pb'
require 'socket'

CONF = "dist_subs.conf"
SERVER_PORT1 = 5002

class Server
  def initialize(port)
    @server = TCPServer.new(port)
    puts "Admin server #{port} portunda calisiyor."
  end

  def start
    loop do
    recv = @server.accept
    handle_jserver(recv)
    end
  end

  def handle_jserver(recv)
    puts "İstemci ile bağlantı kuruldu."

    #dist_subs.conf dosyası okundu
    value = 0
    File.open(CONF, 'r') do |file|
      file.each_line do |line|
        arg = line.split('=', 2)
        value = arg[1].to_i
      end
    end

    begin
      #Config dosyasından okunan veriyi java serverına yollama
      response = Configuration.new(fault_tolerance_level: value, method_type: MethodType::STRT)
      data = response.to_proto
      recv.write([data.size].pack("N") + data)
      puts "Gönderildi"

      #Java serverından gelen başarılı başarısız message nesnesi
      length = recv.read(4).unpack("N")[0]
      rmessage = recv.read(length)
      message = Message.decode(rmessage)
      puts "Alindi: #{message.demand}, #{message.response}"

      #Java serverından capacity nesnesi talebi
      cpctyInquiry = Message.new(demand: Demand::CPCTY, response: Response::NULL)
      cpctyData = cpctyInquiry.to_proto
      recv.write([cpctyData.size].pack("N") + cpctyData)
      puts "Gönderildi"

      #Java serverından gelen capacity nesnesi
      capacityLength = recv.read(4).unpack("N")[0]
      receivedCapacity = recv.read(capacityLength)
      receivedCpcty = Capacity.decode(receivedCapacity)
      puts "Alindi: #{receivedCpcty.serverX_status}, #{receivedCpcty.timestamp}"

      #Python'a capacity nesnesi gönderimi(Geçici olarak burda)
      cpctyPlotter = Capacity.new(serverX_status: receivedCpcty.serverX_status, timestamp: receivedCpcty.timestamp)
      plotterData = cpctyPlotter.to_proto
      recv.write([plotterData.size].pack("N") + plotterData)
      puts "Gönderildi"

    rescue => e
      puts "Bir hata oluştu: #{e.message}"
    ensure
      recv.close
    end
  end
end

server = Server.new(SERVER_PORT1)
server.start
