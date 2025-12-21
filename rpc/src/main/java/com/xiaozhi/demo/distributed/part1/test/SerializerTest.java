package com.xiaozhi.demo.distributed.part1.test;

import com.xiaozhi.demo.distributed.part1.model.RpcResp;
import com.xiaozhi.demo.distributed.part1.serialization.ProtobufSeriallizer;
import com.xiaozhi.demo.distributed.part1.serialization.Serializer;

/**
 *
 * @author DD
 */
public class SerializerTest {

    public static void main(String[] args) throws Exception {
        Serializer serializer = new ProtobufSeriallizer();
        RpcResp success = RpcResp.success("xxxx");
        byte[] bytes = serializer.OjbectToBytes(success);
        System.out.println(bytes.length);
        
        RpcResp rpcResp = serializer.bytesToObject(bytes, RpcResp.class);
        System.out.println(rpcResp);
    }
}
