package com.xiaozhi.demo.netty.part2.handler;

import com.xiaozhi.demo.netty.part2.model.RpcReq;
import com.xiaozhi.demo.netty.part2.model.RpcResp;
import com.xiaozhi.demo.netty.part2.provider.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 *
 * @author DD
 */
public class NIORPCServerHandler extends SimpleChannelInboundHandler<RpcReq> {

    private final ServiceProvider serviceProvider;

    public NIORPCServerHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcReq rpcReq) throws Exception {
        // 通过请求参数反射调用执行方法
        Object service = serviceProvider.getService(rpcReq.getInterfaceName());
        Method method = service.getClass().getDeclaredMethod(rpcReq.getMethodName(),
                rpcReq.getParameterTypes());
        Object returnResult = method.invoke(service, rpcReq.getParameters());
        ctx.channel().writeAndFlush(RpcResp.success(returnResult));
        ctx.close();
    }
}
