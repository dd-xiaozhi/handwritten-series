package com.xiaozhi.demo.bio.part2.server;

import com.xiaozhi.demo.bio.part2.model.User;
import com.xiaozhi.demo.bio.part2.service.UserService;

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
