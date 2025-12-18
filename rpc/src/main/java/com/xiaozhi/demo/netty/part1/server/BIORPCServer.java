package com.xiaozhi.demo.netty.part1.server;

import com.xiaozhi.demo.netty.part1.model.RpcReq;
import com.xiaozhi.demo.netty.part1.model.RpcResp;
import com.xiaozhi.demo.netty.part1.provider.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author DD
 */
public class BIORPCServer implements RPCServer {

    private ServerSocket ss;
    private final ServiceProvider serviceProvider;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public BIORPCServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        try {
            ss = new ServerSocket(port);
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true) {
                Socket socket = ss.accept();
                if (Thread.interrupted()) {
                    return;
                }
                executorService.execute(new RPCServerWorkRunnable(socket, serviceProvider));
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败");
        }
    }

    @Override
    public void stop() {
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class RPCServerWorkRunnable implements Runnable {

        private final Socket socket;
        private final ServiceProvider serviceProvider;

        public RPCServerWorkRunnable(Socket socket, ServiceProvider serviceProvider) {
            this.socket = socket;
            this.serviceProvider = serviceProvider;
        }

        @Override
        public void run() {

            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                // 读取客户端传过来的id
                RpcReq rpcReq = (RpcReq) ois.readObject();
                System.out.println("服务器接收参数：" + rpcReq);

                // 通过请求参数反射调用执行方法
                Object service = serviceProvider.getService(rpcReq.getInterfaceName());
                Method method = service.getClass().getDeclaredMethod(rpcReq.getMethodName(),
                        rpcReq.getParameterTypes());
                Object returnResult = method.invoke(service, rpcReq.getParameters());

                // 写入User对象给客户端
                oos.writeObject(RpcResp.builder().code("00000").message("ok").data(returnResult).build());
                oos.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
