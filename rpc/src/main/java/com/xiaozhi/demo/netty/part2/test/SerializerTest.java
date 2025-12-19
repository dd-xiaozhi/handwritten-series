package com.xiaozhi.demo.netty.part2.test;

import com.xiaozhi.demo.netty.part2.serialization.ProtobufSeriallizer;
import com.xiaozhi.demo.netty.part2.serialization.Serializer;
import com.xiaozhi.demo.serialization.protobuf.Test;

/**
 *
 * @author DD
 */
public class SerializerTest {

    public static void main(String[] args) throws Exception {
        Serializer serializer = new ProtobufSeriallizer();
        Test test = Test.newBuilder().setName("xiaozhi").setAge(19).build();
        byte[] bytes = serializer.OjbectToBytes(test);
        System.out.println(bytes.length);
        System.out.println(serializer.bytesToObject(bytes, Test.class));
    }
}
