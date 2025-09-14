package com.xiaozhi.demo;

import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.Controller;
import com.xiaozhi.demo.annotation.RequestMapping;
import com.xiaozhi.demo.annotation.ResponseBody;

/**
 *
 * @author DD
 */
@Component
@Controller
@RequestMapping("/test")
public class TestController {

    @ResponseBody
    @RequestMapping("/a")
    public String testA() {
        return "hello world";
    }

}
