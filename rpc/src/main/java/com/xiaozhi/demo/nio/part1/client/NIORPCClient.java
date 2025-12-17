package com.xiaozhi.demo.nio.part1.client;

import com.xiaozhi.demo.nio.part1.common.CommonConstants;
import com.xiaozhi.demo.nio.part1.handler.NIOClientChannelInitializer;
import com.xiaozhi.demo.nio.part1.model.RpcReq;
import com.xiaozhi.demo.nio.part1.model.RpcResp;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NIORPCClient implements RPCClient {

    private final String host;
    private final int port;
    public final Bootstrap bootstrap;
    public final EventLoopGroup group;

    public NIORPCClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();

        initBootstrap();
    }

    private void initBootstrap() {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new NIOClientChannelInitializer());
    }

    @Override
    public RpcResp send(RpcReq rpcReq) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcReq);

            // 阻塞等待结果
            channel.closeFuture().sync();
            RpcResp rpcResp = channel.attr(CommonConstants.RPC_RESP_ATTRIBUTE_KEY).get();
            System.out.println("服务端返回数据" + rpcResp);
            return rpcResp;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}