package com.xiaozhi.demo.nio.part1.test;

import com.xiaozhi.demo.nio.part1.client.NIORPCClient;
import com.xiaozhi.demo.nio.part1.client.RPCClient;
import com.xiaozhi.demo.nio.part1.model.Order;
import com.xiaozhi.demo.nio.part1.model.User;
import com.xiaozhi.demo.nio.part1.proxy.ClientProxy;
import com.xiaozhi.demo.nio.part1.service.OrderService;
import com.xiaozhi.demo.nio.part1.service.UserService;

/**
 *
 * @author DD
 */
public class ClientTest {

    public static void main(String[] args) {
//        RPCClient rpcClient = new BIORPCClient("127.0.0.1", 8089);
        RPCClient rpcClient = new NIORPCClient("127.0.0.1", 8089);
        ClientProxy clientProxy = new ClientProxy(rpcClient);
        User user = clientProxy.getProxy(UserService.class).getUserById(1);
        System.out.println("服务端请求返回数据" + user);

        Order order = clientProxy.getProxy(OrderService.class).findOrderByOrderId(1);
        System.out.println("服务端请求返回数据" + order);
    }
}
