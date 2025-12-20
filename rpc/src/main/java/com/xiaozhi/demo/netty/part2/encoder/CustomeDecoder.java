package com.xiaozhi.demo.netty.part2.encoder;

import com.xiaozhi.demo.netty.part2.enums.MessageType;
import com.xiaozhi.demo.netty.part2.model.RpcReq;
import com.xiaozhi.demo.netty.part2.model.RpcResp;
import com.xiaozhi.demo.netty.part2.serialization.SerializeType;
import com.xiaozhi.demo.netty.part2.serialization.Serializer;
import com.xiaozhi.demo.netty.part2.serialization.SerializerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 自定义解码器
 * 
 * @author DD
 */
@AllArgsConstructor
public class CustomeDecoder extends ByteToMessageDecoder {
    
    private final SerializerContext serializerContext;
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, 
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        // 获取消息类型
        int messageType = byteBuf.readShort();
        Class<?> rpcMdeolClass;
        if (messageType == MessageType.REQUEST.getCode()) {
            rpcMdeolClass = RpcReq.class;
        } else if (messageType == MessageType.RESPONSE.getCode()) {
            rpcMdeolClass = RpcResp.class;
        } else {
            throw new IllegalArgumentException("Unknown message type: " + messageType);
        }

        // 获取序列化方式
        int serializeType = byteBuf.readInt();
        
        // 获取消息长度
        int contentLength = byteBuf.readInt();

        // 获取数据
        byte[] contentBytes = new byte[contentLength];
        byteBuf.readBytes(contentBytes);


        Serializer serializer = serializerContext.getSerializer(SerializeType.getByCode(serializeType));
        Object obj = serializer.bytesToObject(contentBytes, rpcMdeolClass);
        list.add(obj);
    }
}
