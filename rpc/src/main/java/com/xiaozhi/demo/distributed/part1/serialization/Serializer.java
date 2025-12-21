package com.xiaozhi.demo.distributed.part1.serialization;

/**
 *
 *
 * @author DD
 */
public interface Serializer {

    /**
     * 对象序列化字节数组
     *
     * @param obj 对象
     * @return 字节数组
     */
    byte[] OjbectToBytes(Object obj) throws Exception;


    /**
     * 字节数组反序列化为对象
     *
     * @param bytes 字节数组
     * @param clazz 反序列化对象类型
     * @return 反序列化对象
     */
    <T> T bytesToObject(byte[] bytes, Class<T> clazz) throws Exception;

    /**
     * 序列化类型
     *
     * @return 序列化类型
     */
    int type();
}
