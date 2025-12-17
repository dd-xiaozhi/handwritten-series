package com.xiaozhi.demo.nio.part1.service;

import com.xiaozhi.demo.nio.part1.model.Order;

/**
 *
 * @author DD
 */
public interface OrderService {

    Order findOrderByOrderId(Integer orderId);
}
