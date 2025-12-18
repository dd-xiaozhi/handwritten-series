package com.xiaozhi.demo.netty.part2.service;

import com.xiaozhi.demo.netty.part2.model.Order;

/**
 *
 * @author DD
 */
public interface OrderService {

    Order findOrderByOrderId(Integer orderId);
}
