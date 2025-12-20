package com.xiaozhi.demo.netty.part2.test;

import com.xiaozhi.demo.netty.part2.client.NIORPCClient;
import com.xiaozhi.demo.netty.part2.client.RPCClient;
import com.xiaozhi.demo.netty.part2.model.Order;
import com.xiaozhi.demo.netty.part2.model.User;
import com.xiaozhi.demo.netty.part2.proxy.ClientProxy;
import com.xiaozhi.demo.netty.part2.serialization.SerializeType;
import com.xiaozhi.demo.netty.part2.service.OrderService;
import com.xiaozhi.demo.netty.part2.service.UserService;

/**
 *
 * @author DD
 */
public class ClientTest {

    public static void main(String[] args) {
//        RPCClient rpcClient = new BIORPCClient("127.0.0.1", 8089);
        RPCClient rpcClient = new NIORPCClient("127.0.0.1", 8089);
        ClientProxy clientProxy = new ClientProxy(rpcClient, SerializeType.JAVA_OBJECT);
        User user = clientProxy.getProxy(UserService.class).getUserById(1);
        System.out.println("服务端请求返回数据" + user);

        Order order = clientProxy.getProxy(OrderService.class).findOrderByOrderId(1);
        System.out.println("服务端请求返回数据" + order);
    }
}
