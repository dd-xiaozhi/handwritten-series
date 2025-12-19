package com.xiaozhi.demo.netty.part2.serialization;

/**
 *
 * @author DD
 */
public class ProtobufSeriallizer implements Serializer {

    @Override
    public byte[] OjbectToBytes(Object obj) {
        return new byte[0];
    }

    @Override
    public <T> T bytesToObject(byte[] bytes, Class<T> clazz) {
        return null;
    }

    @Override
    public int type() {
        return SerializeTypeEnum.PROTOBUF.getCode();
    }
}
