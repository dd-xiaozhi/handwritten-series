package com.xiaozhi.demo.test;

import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.Controller;
import com.xiaozhi.demo.annotation.RequestMapping;
import com.xiaozhi.demo.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author DD
 */
@Component
@Controller
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/html")
    public String html(HttpServletRequest request) {
        request.setAttribute("content", "test");
        return "forward:index";
    }

    @ResponseBody
    @RequestMapping("/json")
    public User json() {
        return new User("dd", 18);
    }

}
