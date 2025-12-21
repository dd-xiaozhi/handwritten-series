package com.xiaozhi.demo.distributed.part1.serialization;


import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 *
 * @author DD
 */
@SuppressWarnings("unchecked")
public class ProtobufSeriallizer implements Serializer {

    @Override
    public byte[] OjbectToBytes(Object obj) {
        // 检查 null 对象
        if (obj == null) {
            throw new IllegalArgumentException("Cannot serialize null object");
        }
        // 获取对象的 schema
        Schema schema = RuntimeSchema.getSchema(obj.getClass());

        // 使用 LinkedBuffer 来创建缓冲区（默认大小 1024）
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        // 序列化对象为字节数组
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    @Override
    public <T> T bytesToObject(byte[] bytes, Class<T> clazz) throws Exception {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Cannot deserialize null or empty byte array");
        }
        // 获取对象的 schema
        Schema schema = RuntimeSchema.getSchema(clazz);

        // 创建一个空的对象实例
        Object obj = clazz.getDeclaredConstructor().newInstance();

        // 反序列化字节数组为对象
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return (T) obj;
    }

    @Override
    public int type() {
        return SerializeType.PROTOBUF.getCode();
    }
}
