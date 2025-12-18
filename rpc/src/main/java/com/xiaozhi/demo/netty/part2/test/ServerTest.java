package com.xiaozhi.demo.netty.part2.test;

import com.xiaozhi.demo.netty.part2.provider.ServiceProvider;
import com.xiaozhi.demo.netty.part2.server.NIORPCServe;
import com.xiaozhi.demo.netty.part2.server.RPCServer;
import com.xiaozhi.demo.netty.part2.service.OrderServiceServerImpl;
import com.xiaozhi.demo.netty.part2.service.UserServiceServerImpl;

/**
 *
 * @author DD
 */
public class ServerTest {

    public static void main(String[] args) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.addService(new UserServiceServerImpl());
        serviceProvider.addService(new OrderServiceServerImpl());
//        RPCServer server = new BIORPCServer(serviceProvider);
        RPCServer server = new NIORPCServe(serviceProvider);
        server.start(8089);
    }
}
