package com.xiaozhi.demo.bio.part3.model;

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
}
