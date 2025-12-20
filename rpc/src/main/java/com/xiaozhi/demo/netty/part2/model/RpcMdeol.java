package com.xiaozhi.demo.netty.part2.model;

import com.xiaozhi.demo.netty.part2.serialization.SerializeType;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author DD
 */
@Data
public class RpcMdeol implements Serializable {
    
    private SerializeType serializerType;
}
