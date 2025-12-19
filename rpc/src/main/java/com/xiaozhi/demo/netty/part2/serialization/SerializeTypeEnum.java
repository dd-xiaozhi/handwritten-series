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

    JAVA_OBJECT(1, "java object"),
    JSON(2, "json"),
    PROTOBUF(3, "protobuf");

    private final int code;
    private final String name;

    public static SerializeTypeEnum getByCode(int code) {
        for (SerializeTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }

        throw new IllegalArgumentException("code: " + code);
    }
}
