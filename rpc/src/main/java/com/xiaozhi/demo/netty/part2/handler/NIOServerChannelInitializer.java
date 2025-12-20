package com.xiaozhi.demo.netty.part2.handler;

import com.xiaozhi.demo.netty.part2.encoder.CustomeDecoder;
import com.xiaozhi.demo.netty.part2.encoder.CustomeEncoder;
import com.xiaozhi.demo.netty.part2.provider.ServiceProvider;
import com.xiaozhi.demo.netty.part2.serialization.SerializerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

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
        SerializerContext serializerContext = new SerializerContext();
        sc.pipeline()
                // .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                // .addLast(new LengthFieldPrepender(4))
                // 添加对象编码器和解码器
                // .addLast(new ObjectEncoder())
                // .addLast(new ObjectDecoder(Class::forName))
                
                // 使用自定义解码和编码器
                .addLast(new CustomeEncoder(serializerContext))
                .addLast(new CustomeDecoder(serializerContext))
                
                // 添加处理的 handler
                .addLast(new NIORPCServerHandler(serviceProvider))
                ;
    }
}
