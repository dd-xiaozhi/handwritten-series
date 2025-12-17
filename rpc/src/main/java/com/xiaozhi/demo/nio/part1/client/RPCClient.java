package com.xiaozhi.demo.nio.part1.client;

import com.xiaozhi.demo.nio.part1.model.RpcReq;
import com.xiaozhi.demo.nio.part1.model.RpcResp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author DD
 */
public interface RPCClient {

    RpcResp send(RpcReq rpcReq);
}
