package com.xiaozhi.demo.bio.part3.service;

import com.xiaozhi.demo.bio.part3.model.Order;

/**
 *
 * @author DD
 */
public interface OrderService {

    Order findOrderByOrderId(Integer orderId);
}
