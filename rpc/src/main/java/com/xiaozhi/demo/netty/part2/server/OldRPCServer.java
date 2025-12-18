package com.xiaozhi.demo.netty.part2.server;

import com.xiaozhi.demo.netty.part2.model.RpcReq;
import com.xiaozhi.demo.netty.part2.model.RpcResp;
import com.xiaozhi.demo.netty.part2.service.UserService;
import com.xiaozhi.demo.netty.part2.service.UserServiceServerImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author DD
 */
public class OldRPCServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceServerImpl();
        try {
            ServerSocket serverSocket = new ServerSocket(8089);
            System.out.println("服务端启动了");
            // BIO的方式监听Socket
            while (true) {
                Socket socket = serverSocket.accept();
                // 开启一个线程去处理
                new Thread(() -> {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        // 读取客户端传过来的id
                        RpcReq rpcReq = (RpcReq) ois.readObject();
                        System.out.println("服务器接收参数：" + rpcReq);

                        // 通过请求参数反射调用执行方法
                        Method method = UserService.class.getDeclaredMethod(rpcReq.getMethodName(), rpcReq.getParameterTypes());
                        Object obj = method.invoke(userService, rpcReq.getParameters());

                        // 写入User对象给客户端
                        oos.writeObject(RpcResp.builder().code("00000").message("ok").data(obj).build());
                        oos.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }
}
