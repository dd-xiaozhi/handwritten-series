package com.xiaozhi.demo.bio.part3.client;

import com.xiaozhi.demo.bio.part3.model.RpcReq;
import com.xiaozhi.demo.bio.part3.model.RpcResp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RPCClient {

    public static RpcResp send(String host, int port, RpcReq rpcReq) {
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