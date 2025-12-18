package com.xiaozhi.demo.netty.part2.serialization;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author DD
 */
@Getter
@AllArgsConstructor
public enum SerializeTypeEnum {

    JAVA_OBJECT(1, "java object", new ObjectSeriallizer()),
    JSON(2, "json", new JsonSeriallizer()),
    PROTOBUF(3, "protobuf", new ProtobufSeriallizer());

    private final int code;
    private final String name;
    private final Serializer serializer;

    public static SerializeTypeEnum getByCode(int code) {
        for (SerializeTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }

        throw new IllegalArgumentException("code: " + code);
    }
}
