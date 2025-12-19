package com.xiaozhi.demo.netty.part2.serialization;

import com.google.protobuf.Message;

import java.lang.reflect.Method;

/**
 *
 * @author DD
 */
@SuppressWarnings("unchecked")
public class ProtobufSeriallizer implements Serializer {

    @Override
    public byte[] OjbectToBytes(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object is null");
        }

        // 检查是否是 Protobuf Message
        if (!(obj instanceof com.google.protobuf.Message)) {
            throw new IllegalArgumentException(
                    "Object is not a protobuf Message: " + obj.getClass().getName());
        }

        // 调用 toByteArray()
        return ((com.google.protobuf.Message) obj).toByteArray();
    }

    @Override
    public <T> T bytesToObject(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        if (!Message.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class is not a protobuf Message: " + clazz.getName());
        }

        // 反射调用静态方法: YourMessage.parseFrom(byte[])
        try {
            Method parseMethod = clazz.getMethod("parseFrom", byte[].class);
            return (T) parseMethod.invoke(null, bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize protobuf message", e);
        }
    }

    @Override
    public int type() {
        return SerializeTypeEnum.PROTOBUF.getCode();
    }
}
