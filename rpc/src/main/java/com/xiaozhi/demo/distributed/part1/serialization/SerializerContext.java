package com.xiaozhi.demo.distributed.part1.serialization;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DD
 */
public class SerializerContext {

    public final Map<SerializeType, Serializer> serializerMap = new HashMap<>();

    public SerializerContext() {
        SerializeType.getAll()
                .forEach(serializeTypeEnum -> serializerMap.put(serializeTypeEnum,
                        SerializerFactor.getSerializer(serializeTypeEnum.getCode())));
    }
    
    public Serializer getSerializer(SerializeType serializeType) {
        return serializerMap.get(serializeType);
    }
}
