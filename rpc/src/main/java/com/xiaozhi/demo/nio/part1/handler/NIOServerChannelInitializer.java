package com.xiaozhi.demo.nio.part1.handler;

import com.xiaozhi.demo.nio.part1.provider.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 *
 * @author DD
 */
public class NIOServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ServiceProvider serviceProvider;

    public NIOServerChannelInitializer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    protected void initChannel(SocketChannel sc) {
        sc.pipeline()
                // 添加对象编码器和解码器
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(Class::forName))
                // 添加处理的 handler
                .addLast(new NIORPCServerHandler(serviceProvider))
                ;
    }
}
