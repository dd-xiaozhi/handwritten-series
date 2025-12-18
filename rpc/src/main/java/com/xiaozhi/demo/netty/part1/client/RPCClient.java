package com.xiaozhi.demo.netty.part1.client;

import com.xiaozhi.demo.netty.part1.model.RpcReq;
import com.xiaozhi.demo.netty.part1.model.RpcResp;

/**
 *
 * @author DD
 */
public interface RPCClient {

    RpcResp send(RpcReq rpcReq);
}
