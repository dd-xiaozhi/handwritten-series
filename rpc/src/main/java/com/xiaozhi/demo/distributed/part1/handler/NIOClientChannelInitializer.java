package com.xiaozhi.demo.distributed.part1.handler;

import com.xiaozhi.demo.distributed.part1.encoder.CustomeDecoder;
import com.xiaozhi.demo.distributed.part1.encoder.CustomeEncoder;
import com.xiaozhi.demo.distributed.part1.serialization.SerializerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 *
 * @author DD
 */
public class NIOClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        SerializerContext serializerContext = new SerializerContext();
        socketChannel.pipeline()
                // .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                // .addLast(new LengthFieldPrepender(4))
                // 添加对象编码器和解码器
                // .addLast(new ObjectEncoder())
                // .addLast(new ObjectDecoder(Class::forName))
                
                // 使用自定义的编码器和解码器
                .addLast(new CustomeEncoder(serializerContext))
                .addLast(new CustomeDecoder(serializerContext))
                
                .addLast(new NIORPCClientHandler());
    }
}
