// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: dist_servers/Subscriber.proto
// Protobuf Java Version: 4.28.3

package com.protos;

public final class SubscriberJava {
  private SubscriberJava() {}
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 28,
      /* patch= */ 3,
      /* suffix= */ "",
      SubscriberJava.class.getName());
  }
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_dist_servers_Subscriber_descriptor;
  static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_dist_servers_Subscriber_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\035dist_servers/Subscriber.proto\022\014dist_se" +
      "rvers\"\250\001\n\nSubscriber\022\024\n\014name_surname\030\001 \001" +
      "(\t\022\022\n\nstart_Date\030\002 \001(\003\022\025\n\rlast_accessed\030" +
      "\003 \001(\003\022\021\n\tinterests\030\005 \003(\t\022\020\n\010isOnline\030\006 \001" +
      "(\010\022(\n\006demand\030\007 \001(\0162\030.dist_servers.Demand" +
      "Type\022\n\n\002ID\030\010 \001(\003*=\n\nDemandType\022\010\n\004SUBS\020\000" +
      "\022\007\n\003DEL\020\001\022\010\n\004UPDT\020\002\022\010\n\004ONLN\020\003\022\010\n\004OFFL\020\004B" +
      "\036\n\ncom.protosB\016SubscriberJavaP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_dist_servers_Subscriber_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_dist_servers_Subscriber_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_dist_servers_Subscriber_descriptor,
        new java.lang.String[] { "NameSurname", "StartDate", "LastAccessed", "Interests", "IsOnline", "Demand", "ID", });
    descriptor.resolveAllFeaturesImmutable();
  }

  // @@protoc_insertion_point(outer_class_scope)
}