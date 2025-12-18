package com.xiaozhi.demo.netty.part1.client;

import com.xiaozhi.demo.netty.part1.model.RpcReq;
import com.xiaozhi.demo.netty.part1.model.RpcResp;
import lombok.AllArgsConstructor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
public class BIORPCClient implements RPCClient {

    private String host;
    private int port;

    @Override
    public RpcResp send(RpcReq rpcReq) {
        try(Socket socket = new Socket(host, port)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("请求参数：" + rpcReq);

            // 发送请求
            oos.writeObject(rpcReq);
            oos.flush();

            return (RpcResp) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}