package com.xiaozhi.demo.bio.part3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.builder.qual.NotCalledMethods;

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
