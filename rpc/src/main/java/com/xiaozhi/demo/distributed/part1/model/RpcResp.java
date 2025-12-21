package com.xiaozhi.demo.distributed.part1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * @author DD
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RpcResp  extends RpcMdeol implements Serializable {

    private String code = "00000";
    private String message = "ok";
    private Class<?> dataType;
    private Object data;

    public static RpcResp success(Object data) {
        return create(data);
    }

    public static RpcResp fail(String message) {
        RpcResp rpcResp = new RpcResp();
        rpcResp.setCode("99999");
        rpcResp.setMessage(message);
        return rpcResp;
    }
    
    public static RpcResp create(Object data) {
        RpcResp rpcResp = new RpcResp();
        rpcResp.setData(data);
        rpcResp.setDataType(data.getClass());
        return rpcResp;
    }
}
