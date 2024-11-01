// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: Capacity.proto
// Protobuf Java Version: 4.28.3

package com.protos;

/**
 * Protobuf type {@code dist_servers.Capacity}
 */
public final class Capacity extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:dist_servers.Capacity)
    CapacityOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 3,
      /* suffix= */ "",
      Capacity.class.getName());
  }
  // Use Capacity.newBuilder() to construct.
  private Capacity(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private Capacity() {
    serverXStatus_ = emptyIntList();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.protos.CapacityJava.internal_static_dist_servers_Capacity_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.protos.CapacityJava.internal_static_dist_servers_Capacity_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.protos.Capacity.class, com.protos.Capacity.Builder.class);
  }

  public static final int SERVERX_STATUS_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private com.google.protobuf.Internal.IntList serverXStatus_ =
      emptyIntList();
  /**
   * <code>repeated int32 serverX_status = 1;</code>
   * @return A list containing the serverXStatus.
   */
  @java.lang.Override
  public java.util.List<java.lang.Integer>
      getServerXStatusList() {
    return serverXStatus_;
  }
  /**
   * <code>repeated int32 serverX_status = 1;</code>
   * @return The count of serverXStatus.
   */
  public int getServerXStatusCount() {
    return serverXStatus_.size();
  }
  /**
   * <code>repeated int32 serverX_status = 1;</code>
   * @param index The index of the element to return.
   * @return The serverXStatus at the given index.
   */
  public int getServerXStatus(int index) {
    return serverXStatus_.getInt(index);
  }
  private int serverXStatusMemoizedSerializedSize = -1;

  public static final int TIMESTAMP_FIELD_NUMBER = 2;
  private long timestamp_ = 0L;
  /**
   * <code>int64 timestamp = 2;</code>
   * @return The timestamp.
   */
  @java.lang.Override
  public long getTimestamp() {
    return timestamp_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (getServerXStatusList().size() > 0) {
      output.writeUInt32NoTag(10);
      output.writeUInt32NoTag(serverXStatusMemoizedSerializedSize);
    }
    for (int i = 0; i < serverXStatus_.size(); i++) {
      output.writeInt32NoTag(serverXStatus_.getInt(i));
    }
    if (timestamp_ != 0L) {
      output.writeInt64(2, timestamp_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    {
      int dataSize = 0;
      for (int i = 0; i < serverXStatus_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeInt32SizeNoTag(serverXStatus_.getInt(i));
      }
      size += dataSize;
      if (!getServerXStatusList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dataSize);
      }
      serverXStatusMemoizedSerializedSize = dataSize;
    }
    if (timestamp_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, timestamp_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.protos.Capacity)) {
      return super.equals(obj);
    }
    com.protos.Capacity other = (com.protos.Capacity) obj;

    if (!getServerXStatusList()
        .equals(other.getServerXStatusList())) return false;
    if (getTimestamp()
        != other.getTimestamp()) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getServerXStatusCount() > 0) {
      hash = (37 * hash) + SERVERX_STATUS_FIELD_NUMBER;
      hash = (53 * hash) + getServerXStatusList().hashCode();
    }
    hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTimestamp());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.protos.Capacity parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.protos.Capacity parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.protos.Capacity parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.protos.Capacity parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.protos.Capacity parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.protos.Capacity parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.protos.Capacity parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.protos.Capacity parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.protos.Capacity parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.protos.Capacity parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.protos.Capacity parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.protos.Capacity parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.protos.Capacity prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code dist_servers.Capacity}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:dist_servers.Capacity)
      com.protos.CapacityOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.protos.CapacityJava.internal_static_dist_servers_Capacity_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.protos.CapacityJava.internal_static_dist_servers_Capacity_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.protos.Capacity.class, com.protos.Capacity.Builder.class);
    }

    // Construct using com.protos.Capacity.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      serverXStatus_ = emptyIntList();
      timestamp_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.protos.CapacityJava.internal_static_dist_servers_Capacity_descriptor;
    }

    @java.lang.Override
    public com.protos.Capacity getDefaultInstanceForType() {
      return com.protos.Capacity.getDefaultInstance();
    }

    @java.lang.Override
    public com.protos.Capacity build() {
      com.protos.Capacity result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.protos.Capacity buildPartial() {
      com.protos.Capacity result = new com.protos.Capacity(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.protos.Capacity result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        serverXStatus_.makeImmutable();
        result.serverXStatus_ = serverXStatus_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.timestamp_ = timestamp_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.protos.Capacity) {
        return mergeFrom((com.protos.Capacity)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.protos.Capacity other) {
      if (other == com.protos.Capacity.getDefaultInstance()) return this;
      if (!other.serverXStatus_.isEmpty()) {
        if (serverXStatus_.isEmpty()) {
          serverXStatus_ = other.serverXStatus_;
          serverXStatus_.makeImmutable();
          bitField0_ |= 0x00000001;
        } else {
          ensureServerXStatusIsMutable();
          serverXStatus_.addAll(other.serverXStatus_);
        }
        onChanged();
      }
      if (other.getTimestamp() != 0L) {
        setTimestamp(other.getTimestamp());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              int v = input.readInt32();
              ensureServerXStatusIsMutable();
              serverXStatus_.addInt(v);
              break;
            } // case 8
            case 10: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              ensureServerXStatusIsMutable();
              while (input.getBytesUntilLimit() > 0) {
                serverXStatus_.addInt(input.readInt32());
              }
              input.popLimit(limit);
              break;
            } // case 10
            case 16: {
              timestamp_ = input.readInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.Internal.IntList serverXStatus_ = emptyIntList();
    private void ensureServerXStatusIsMutable() {
      if (!serverXStatus_.isModifiable()) {
        serverXStatus_ = makeMutableCopy(serverXStatus_);
      }
      bitField0_ |= 0x00000001;
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @return A list containing the serverXStatus.
     */
    public java.util.List<java.lang.Integer>
        getServerXStatusList() {
      serverXStatus_.makeImmutable();
      return serverXStatus_;
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @return The count of serverXStatus.
     */
    public int getServerXStatusCount() {
      return serverXStatus_.size();
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @param index The index of the element to return.
     * @return The serverXStatus at the given index.
     */
    public int getServerXStatus(int index) {
      return serverXStatus_.getInt(index);
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @param index The index to set the value at.
     * @param value The serverXStatus to set.
     * @return This builder for chaining.
     */
    public Builder setServerXStatus(
        int index, int value) {

      ensureServerXStatusIsMutable();
      serverXStatus_.setInt(index, value);
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @param value The serverXStatus to add.
     * @return This builder for chaining.
     */
    public Builder addServerXStatus(int value) {

      ensureServerXStatusIsMutable();
      serverXStatus_.addInt(value);
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @param values The serverXStatus to add.
     * @return This builder for chaining.
     */
    public Builder addAllServerXStatus(
        java.lang.Iterable<? extends java.lang.Integer> values) {
      ensureServerXStatusIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, serverXStatus_);
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>repeated int32 serverX_status = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearServerXStatus() {
      serverXStatus_ = emptyIntList();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }

    private long timestamp_ ;
    /**
     * <code>int64 timestamp = 2;</code>
     * @return The timestamp.
     */
    @java.lang.Override
    public long getTimestamp() {
      return timestamp_;
    }
    /**
     * <code>int64 timestamp = 2;</code>
     * @param value The timestamp to set.
     * @return This builder for chaining.
     */
    public Builder setTimestamp(long value) {

      timestamp_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>int64 timestamp = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTimestamp() {
      bitField0_ = (bitField0_ & ~0x00000002);
      timestamp_ = 0L;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:dist_servers.Capacity)
  }

  // @@protoc_insertion_point(class_scope:dist_servers.Capacity)
  private static final com.protos.Capacity DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.protos.Capacity();
  }

  public static com.protos.Capacity getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Capacity>
      PARSER = new com.google.protobuf.AbstractParser<Capacity>() {
    @java.lang.Override
    public Capacity parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<Capacity> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Capacity> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.protos.Capacity getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

