package com.xiaozhi.demo.netty.part2.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author DD
 */
@Data
@Builder
public class RpcResp  implements Serializable {

    private String code;
    private String message;
    private Object data;

    public static RpcResp success(Object data) {
        return RpcResp.builder().code("00000").message("ok").data(data).build();
    }

    public static RpcResp fail(String message) {
        return RpcResp.builder().code("99999").message(message).build();
    }
}
