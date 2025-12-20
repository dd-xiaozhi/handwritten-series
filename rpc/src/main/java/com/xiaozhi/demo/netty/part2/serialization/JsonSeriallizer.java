package com.xiaozhi.demo.netty.part2.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.xiaozhi.demo.netty.part2.model.RpcReq;
import com.xiaozhi.demo.netty.part2.model.RpcResp;

import java.util.Objects;

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
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        Object resultObj;
        if (RpcReq.class.equals(clazz)) {
            resultObj = parseRpcRequest(bytes);
        } else if (RpcResp.class.equals(clazz)) {
            resultObj = parseRpcResponse(bytes);
        } else {
            throw new IllegalArgumentException("Unknown message type: " + clazz);
        }

        return (T) resultObj;
    }

    private Object parseRpcRequest(byte[] bytes) {
        RpcReq rpcReq = JSON.parseObject(bytes, RpcReq.class, JSONReader.Feature.SupportClassForName);
        Object[] objects = new Object[rpcReq.getParameters().length];
        // 把json字串转化成对应的对象， fastjson可以读出基本数据类型，不用转化
        // 对转换后的request中的params属性逐个进行类型判断
        for (int i = 0; i < objects.length; i++) {
            Class<?> paramsType = rpcReq.getParameterTypes()[i];
            // 判断每个对象类型是否和paramsTypes中的一致
            if (!paramsType.isAssignableFrom(rpcReq.getParameters()[i].getClass())) {
                // 如果不一致，就行进行类型转换
                objects[i] = JSON.toJavaObject(rpcReq.getParameters()[i], rpcReq.getParameterTypes()[i]);
            } else {
                // 如果一致就直接赋给objects[i]
                objects[i] = rpcReq.getParameters()[i];
            }
        }
        rpcReq.setParameters(objects);
        return rpcReq;
    }

    private Object parseRpcResponse(byte[] bytes) {
        RpcResp rpcResp = JSON.parseObject(bytes, RpcResp.class, JSONReader.Feature.SupportClassForName);
        if (Objects.isNull(rpcResp.getDataType())) {
            return RpcResp.fail("类型为空");
        }
        
        Class<?> dataType = rpcResp.getDataType();
        // 判断转化后的response对象中的data的类型是否正确
        if (rpcResp.getData() != null 
                && !dataType.isAssignableFrom(rpcResp.getData().getClass())) {
            rpcResp.setData(JSON.toJavaObject(rpcResp.getData(), dataType));
        }
        return rpcResp;
    }


    @Override
    public int type() {
        return SerializeType.JSON.getCode();
    }
}
