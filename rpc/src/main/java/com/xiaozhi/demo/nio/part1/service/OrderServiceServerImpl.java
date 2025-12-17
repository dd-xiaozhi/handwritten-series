package com.xiaozhi.demo.nio.part1.service;

import com.xiaozhi.demo.nio.part1.model.Order;

/**
 *
 * @author DD
 */
public class OrderServiceServerImpl implements OrderService {

    @Override
    public Order findOrderByOrderId(Integer orderId) {
        return new Order("order_xxxx_111", "user_xxxx_111", "2021-01-01");
    }
}
