package com.xiaozhi.demo.mvc;

import com.xiaozhi.demo.annotation.Component;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import lombok.SneakyThrows;

/**
 * 调度器
 * 处理请求的流程在调度器中实现
 *
 * @author DD
 */
@Component
public class DispatchServlet extends HttpServlet {

    @SneakyThrows
    @Override
    public void service(ServletRequest req, ServletResponse resp) {
        // 获取处理当前请求的 handler

        // 前置拦截

        // 执行 handler 的处理方法


        // 响应数据
        // 后置拦截

    }


}
