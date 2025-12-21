package com.xiaozhi.demo.distributed.part1.model;

import com.xiaozhi.demo.distributed.part1.serialization.SerializeType;
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
