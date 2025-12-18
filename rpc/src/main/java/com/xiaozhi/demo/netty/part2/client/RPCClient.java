package com.xiaozhi.demo.netty.part2.client;

import com.xiaozhi.demo.netty.part2.model.RpcReq;
import com.xiaozhi.demo.netty.part2.model.RpcResp;

/**
 *
 * @author DD
 */
public interface RPCClient {

    RpcResp send(RpcReq rpcReq);
}
