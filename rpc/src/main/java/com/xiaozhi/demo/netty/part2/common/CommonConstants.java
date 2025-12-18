package com.xiaozhi.demo.netty.part2.common;

import com.xiaozhi.demo.netty.part2.model.RpcResp;
import io.netty.util.AttributeKey;

/**
 *
 * @author DD
 */
public class CommonConstants {
    
    public static final AttributeKey<RpcResp> RPC_RESP_ATTRIBUTE_KEY = AttributeKey.valueOf("RpcResp");
}
