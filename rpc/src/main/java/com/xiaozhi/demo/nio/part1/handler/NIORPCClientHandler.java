package com.xiaozhi.demo.nio.part1.handler;

import com.xiaozhi.demo.nio.part1.common.CommonConstants;
import com.xiaozhi.demo.nio.part1.model.RpcResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * @author DD
 */
public class NIORPCClientHandler extends SimpleChannelInboundHandler<RpcResp> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResp rpcResp) {
        ctx.channel().attr(CommonConstants.RPC_RESP_ATTRIBUTE_KEY).set(rpcResp);
        ctx.close();
    }
}
