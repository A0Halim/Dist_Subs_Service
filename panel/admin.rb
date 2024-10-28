require_relative 'configuration_pb'
require_relative 'capacity_pb'
require_relative 'message_pb'

def read_conf(conf_name)
  value = 0
  File.open(conf_name, 'r') do |file|
    file.each_line do |line|  
    arg = line.split('=', 2)
    value = arg[1].to_i
    end
  end
  value
end

value = read_conf("dist_subs.conf")

config = Configuration.new(
  fault_tolerance_level: value,
  method_type: MethodType::STRT
)

serialized_data = config.to_proto
