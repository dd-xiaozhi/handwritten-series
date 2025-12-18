package com.xiaozhi.demo.netty.part1.service;

import com.xiaozhi.demo.netty.part1.model.Order;

/**
 *
 * @author DD
 */
public interface OrderService {

    Order findOrderByOrderId(Integer orderId);
}
