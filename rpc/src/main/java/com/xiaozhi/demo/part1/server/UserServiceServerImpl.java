package com.xiaozhi.demo.part1.server;

import com.xiaozhi.demo.part1.model.User;
import com.xiaozhi.demo.part1.service.UserService;

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
