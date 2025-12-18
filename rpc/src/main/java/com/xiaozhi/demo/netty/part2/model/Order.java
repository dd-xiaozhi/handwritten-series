package com.xiaozhi.demo.netty.part2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author DD
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    private String orderId;
    private String productId;
    private String userId;
}
