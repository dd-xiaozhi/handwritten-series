package com.xiaozhi.demo.netty.part2.service;

import com.xiaozhi.demo.netty.part2.model.User;

/**
 * @author DD
 */
public class UserServiceServerImpl implements UserService {

    @Override
    public User getUserById(Integer id) {
        System.out.println("查询客户， id: " + id);
        return new User(1, "DD", true);
    }
}
