package com.xiaozhi.demo.distributed.part1.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author DD
 */
@Getter
@AllArgsConstructor
public enum MessageType {
    
    REQUEST(1),
    RESPONSE(2);
    
    private final int code;
    
    public static final Map<Integer, MessageType> MESSAGE_TYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(MessageType::getCode, Function.identity()));
    
    public static MessageType getByCode(int code) {
        if (!MESSAGE_TYPE_MAP.containsKey(code)) {
            throw new IllegalArgumentException("code: " + code);
        }

        return MESSAGE_TYPE_MAP.get(code);
    }
}
