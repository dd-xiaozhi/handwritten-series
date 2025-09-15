package com.xiaozhi.demo.test;

import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.Controller;
import com.xiaozhi.demo.annotation.RequestMapping;
import com.xiaozhi.demo.annotation.ResponseBody;

/**
 * @author DD
 */
@Component
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/html")
    public String html() {
        return "<h1>hello word</h1>";
    }

    @ResponseBody
    @RequestMapping("/json")
    public User json() {
        return new User("dd", 18);
    }

}
