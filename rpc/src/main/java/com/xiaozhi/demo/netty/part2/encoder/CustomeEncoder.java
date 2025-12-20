package com.xiaozhi.demo.netty.part2.encoder;

import com.xiaozhi.demo.netty.part2.enums.MessageType;
import com.xiaozhi.demo.netty.part2.model.RpcReq;
import com.xiaozhi.demo.netty.part2.model.RpcResp;
import com.xiaozhi.demo.netty.part2.serialization.SerializeType;
import com.xiaozhi.demo.netty.part2.serialization.Serializer;
import com.xiaozhi.demo.netty.part2.serialization.SerializerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * 自定义解码器
 * 
 * @author DD
 */
@AllArgsConstructor
public class CustomeEncoder extends MessageToByteEncoder {
    
    private final SerializerContext serializerContext;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, 
                          Object rpcMdeol, ByteBuf byteBuf) throws Exception {
        int messageType;
        SerializeType serializerType;
        if (rpcMdeol instanceof RpcReq) {
            messageType = MessageType.REQUEST.getCode();
            serializerType = ((RpcReq) rpcMdeol).getSerializerType();
        } else if (rpcMdeol instanceof RpcResp) {
            messageType = MessageType.RESPONSE.getCode();
            serializerType = ((RpcResp) rpcMdeol).getSerializerType();
        } else {
            throw new IllegalArgumentException("Unknown message type: " + rpcMdeol);
        }

        // SerializeType serializerType = rpcMdeol.getSerializerType();
        if (Objects.isNull(serializerType)) {
            throw new IllegalArgumentException("serializer type is null");
        }
        Serializer serializer = serializerContext.getSerializer(serializerType);
        byte[] dataBytes = serializer.OjbectToBytes(rpcMdeol);

        // 消息类型
        byteBuf.writeShort(messageType);
        // 序列化方式
        byteBuf.writeInt(serializerType.getCode());
        // 数据长度
        byteBuf.writeInt(dataBytes.length);
        // 数据
        byteBuf.writeBytes(dataBytes);
    }
}
