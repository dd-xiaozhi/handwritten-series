package com.xiaozhi.demo.bio.part3.test;

import com.xiaozhi.demo.bio.part3.provider.ServiceProvider;
import com.xiaozhi.demo.bio.part3.server.RPCServer;
import com.xiaozhi.demo.bio.part3.server.SimpleRPCServer;
import com.xiaozhi.demo.bio.part3.service.OrderServiceServerImpl;
import com.xiaozhi.demo.bio.part3.service.UserServiceServerImpl;

/**
 *
 * @author DD
 */
public class ServerTest {

    public static void main(String[] args) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.addService(new UserServiceServerImpl());
        serviceProvider.addService(new OrderServiceServerImpl());
        RPCServer server = new SimpleRPCServer(serviceProvider);
        server.start(8089);
    }
}
