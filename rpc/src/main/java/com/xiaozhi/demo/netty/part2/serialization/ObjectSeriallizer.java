package com.xiaozhi.demo.netty.part2.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * java 原生对象序列化
 *
 * @author DD
 */
public class ObjectSeriallizer implements Serializer {

    @Override
    public byte[] OjbectToBytes(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    @Override
    public <T> T bytesToObject(byte[] bytes, Class<T> clazz) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        }
    }

    @Override
    public int type() {
        return SerializeTypeEnum.JAVA_OBJECT.getCode();
    }
}
