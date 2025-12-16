package com.xiaozhi.demo.part2.proxy;

import com.xiaozhi.demo.part2.client.RPCClient;
import com.xiaozhi.demo.part2.model.RpcReq;
import com.xiaozhi.demo.part2.model.RpcResp;
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

    private String host;
    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        RpcReq rpcReq = RpcReq.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .parameterTypes(method.getParameterTypes())
                .build();

        RpcResp rpcResp = RPCClient.send(host, port, rpcReq);
        if (!rpcResp.getCode().equals("00000")) {
            throw new RuntimeException(rpcResp.getMessage());
        }
        return rpcResp.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
}
