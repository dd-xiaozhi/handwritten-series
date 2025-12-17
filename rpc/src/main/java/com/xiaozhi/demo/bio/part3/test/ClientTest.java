package com.xiaozhi.demo.bio.part3.test;

import com.xiaozhi.demo.bio.part3.model.Order;
import com.xiaozhi.demo.bio.part3.model.User;
import com.xiaozhi.demo.bio.part3.proxy.ClientProxy;
import com.xiaozhi.demo.bio.part3.service.OrderService;
import com.xiaozhi.demo.bio.part3.service.UserService;

/**
 *
 * @author DD
 */
public class ClientTest {

    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8089);
        User user = clientProxy.getProxy(UserService.class).getUserById(1);
        System.out.println("服务端请求返回数据" + user);

        Order order = clientProxy.getProxy(OrderService.class).findOrderByOrderId(1);
        System.out.println("服务端请求返回数据" + order);
    }
}
