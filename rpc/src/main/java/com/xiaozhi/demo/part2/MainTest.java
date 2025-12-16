package com.xiaozhi.demo.part2;

import com.xiaozhi.demo.part2.model.User;
import com.xiaozhi.demo.part2.proxy.ClientProxy;
import com.xiaozhi.demo.part2.service.UserService;

/**
 *
 * @author DD
 */
public class MainTest {

    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 8089);
        UserService userService = clientProxy.getProxy(UserService.class);

        User user = userService.getUserById(1);
        System.out.println("服务器响应数据：" + user);
        System.out.println(user);
    }
}
