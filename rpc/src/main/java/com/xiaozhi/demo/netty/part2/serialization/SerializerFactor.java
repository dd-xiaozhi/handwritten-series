package com.xiaozhi.demo.netty.part2.serialization;

/**
 *
 * @author DD
 */
public class SerializerFactor {
    
    public static Serializer getSerializer(int code) {
        SerializeType type = SerializeType.getByCode(code);
        return switch (type) {
            case JAVA_OBJECT -> new ObjectSeriallizer();
            case JSON -> new JsonSeriallizer();
            case PROTOBUF -> new ProtobufSeriallizer();
        };
    }
}
