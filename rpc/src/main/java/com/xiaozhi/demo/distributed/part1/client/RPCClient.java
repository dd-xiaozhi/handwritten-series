package com.xiaozhi.demo.distributed.part1.client;

import com.xiaozhi.demo.distributed.part1.model.RpcReq;
import com.xiaozhi.demo.distributed.part1.model.RpcResp;

/**
 *
 * @author DD
 */
public interface RPCClient {

    RpcResp send(RpcReq rpcReq);
}
