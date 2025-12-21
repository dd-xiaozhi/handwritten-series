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
public class RpcReq extends RpcMdeol implements Serializable {

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
