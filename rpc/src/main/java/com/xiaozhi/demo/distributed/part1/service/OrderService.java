package com.xiaozhi.demo.distributed.part1.service;

import com.xiaozhi.demo.distributed.part1.model.Order;

/**
 *
 * @author DD
 */
public interface OrderService {

    Order findOrderByOrderId(Integer orderId);
}
