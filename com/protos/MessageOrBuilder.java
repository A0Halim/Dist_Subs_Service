// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: dist_servers/Message.proto
// Protobuf Java Version: 4.28.3

package com.protos;

public interface MessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:dist_servers.Message)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.dist_servers.Demand demand = 1;</code>
   * @return The enum numeric value on the wire for demand.
   */
  int getDemandValue();
  /**
   * <code>.dist_servers.Demand demand = 1;</code>
   * @return The demand.
   */
  com.protos.Demand getDemand();

  /**
   * <code>.dist_servers.Response response = 2;</code>
   * @return The enum numeric value on the wire for response.
   */
  int getResponseValue();
  /**
   * <code>.dist_servers.Response response = 2;</code>
   * @return The response.
   */
  com.protos.Response getResponse();
}
