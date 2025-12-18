package com.xiaozhi.demo.serialization.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 *
 * @author DD
 */
public class TestDemo {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Test test = Test.newBuilder()
                .setName("小智")
                .setAge(18)
                .build();
        
        // 将对象序列化成字节数组
        byte[] bytes = test.toByteArray();
        System.out.println("protoBuf 码流长度：" + bytes.length);
        
        // 字节反序列化成对象
        Test test1 = Test.parseFrom(bytes);
        System.out.println(test1.getName());
    }
}
