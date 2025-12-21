package com.xiaozhi.demo.distributed.part1.serialization;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author DD
 */
@Getter
@AllArgsConstructor
public enum SerializeType {

    JAVA_OBJECT(1, "java object"),
    JSON(2, "json"),
    PROTOBUF(3, "protobuf");

    private final int code;
    private final String name;

    public static final Map<Integer, SerializeType> SERIALIZE_TYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(SerializeType::getCode, Function.identity()));

    public static SerializeType getByCode(int code) {
        if (!SERIALIZE_TYPE_MAP.containsKey(code)) {
            throw new IllegalArgumentException("code: " + code);
        }

        return SERIALIZE_TYPE_MAP.get(code);
    }
    
    public static List<SerializeType> getAll() {
        return Arrays.asList(values());
    }
}
