// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: Capacity.proto
// Protobuf Java Version: 4.28.3

package com.protos;

public interface CapacityOrBuilder extends
    // @@protoc_insertion_point(interface_extends:dist_servers.Capacity)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated int32 serverX_status = 1;</code>
   * @return A list containing the serverXStatus.
   */
  java.util.List<java.lang.Integer> getServerXStatusList();
  /**
   * <code>repeated int32 serverX_status = 1;</code>
   * @return The count of serverXStatus.
   */
  int getServerXStatusCount();
  /**
   * <code>repeated int32 serverX_status = 1;</code>
   * @param index The index of the element to return.
   * @return The serverXStatus at the given index.
   */
  int getServerXStatus(int index);

  /**
   * <code>int64 timestamp = 2;</code>
   * @return The timestamp.
   */
  long getTimestamp();
}
