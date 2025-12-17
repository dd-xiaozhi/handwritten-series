package com.xiaozhi.demo.nio.part1.client;

import com.xiaozhi.demo.nio.part1.model.RpcReq;
import com.xiaozhi.demo.nio.part1.model.RpcResp;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class NIORPCClient implements RPCClient {

    private final String host;
    private final int port;
    public final Bootstrap bootstrap;
    public final EventLoopGroup group;
    public static final AttributeKey<Object> RPC_RESP_ATTRIBUTE_KEY = AttributeKey.valueOf("RpcResp");

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
                .handler(new SimpleChannelInboundHandler<RpcResp>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, RpcResp rpcResp) {
                        ctx.channel().attr(RPC_RESP_ATTRIBUTE_KEY).set(rpcResp);
                        ctx.close();
                    }
                });
    }

    @Override
    public RpcResp send(RpcReq rpcReq) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcReq);

            // 阻塞等待结果
            channel.closeFuture().sync();
            return (RpcResp) channel.attr(RPC_RESP_ATTRIBUTE_KEY).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}