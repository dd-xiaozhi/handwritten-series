package com.xiaozhi.demo.netty.part2.serialization;

import com.alibaba.fastjson2.JSON;

/**
 * Json 序列化
 *
 * @author DD
 */
public class JsonSeriallizer implements Serializer {

    @Override
    public byte[] OjbectToBytes(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T bytesToObject(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
