package com.xiaozhi.demo.netty.part2.server;

import com.xiaozhi.demo.netty.part2.handler.NIOServerChannelInitializer;
import com.xiaozhi.demo.netty.part2.provider.ServiceProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author DD
 */
public class NIORPCServe implements RPCServer {

    public final ServerBootstrap serverBootstrap;
    public final EventLoopGroup bossGroup;
    public final EventLoopGroup workerGroup;
    private final ServiceProvider serviceProvider;

    public NIORPCServe(ServiceProvider serviceProvider) {
        this.serverBootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.serviceProvider = serviceProvider;
        initServerBootstrap();
    }

    private void initServerBootstrap() {
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NIOServerChannelInitializer(serviceProvider));
    }

    @Override
    public void start(int port) {
        try {
            // 启动并阻塞等待处理请求
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 阻塞等待服务器管理
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
