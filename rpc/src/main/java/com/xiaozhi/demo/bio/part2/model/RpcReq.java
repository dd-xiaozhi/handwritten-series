package com.xiaozhi.demo.bio.part2.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author DD
 */
@Data
@Builder
public class RpcReq implements Serializable {

    /**
     * 调用接口名
     */
    private String interfaceName;

    /**
     * 调用方法名
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数值
     */
    private Object[] parameters;
}
