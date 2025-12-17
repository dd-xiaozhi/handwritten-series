package com.xiaozhi.demo.bio.part3.server;

/**
 *
 * @author DD
 */
public interface RPCServer {

    /**
     * 启动服务
     * @param port 端口号
     */
    void start(int port);

    /**
     * 停止服务
     */
    void stop();
}
