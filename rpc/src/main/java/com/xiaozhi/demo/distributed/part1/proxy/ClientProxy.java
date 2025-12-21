package com.xiaozhi.demo.distributed.part1.proxy;

import com.xiaozhi.demo.distributed.part1.client.RPCClient;
import com.xiaozhi.demo.distributed.part1.model.RpcReq;
import com.xiaozhi.demo.distributed.part1.model.RpcResp;
import com.xiaozhi.demo.distributed.part1.serialization.SerializeType;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理，代理映射的接口，负责处理rpc 的请求和响应
 *
 * @author DD
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {

    private RPCClient rpcClient;
    private SerializeType serializeType;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        RpcReq rpcReq = new RpcReq();
        rpcReq.setInterfaceName(method.getDeclaringClass().getName());
        rpcReq.setMethodName(method.getName());
        rpcReq.setParameterTypes(method.getParameterTypes());
        rpcReq.setParameters(args);
        rpcReq.setSerializerType(serializeType);
        
        RpcResp rpcResp = rpcClient.send(rpcReq);
        if (!rpcResp.getCode().equals("00000")) {
            throw new RuntimeException(rpcResp.getMessage());
        }
        return rpcResp.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
}
