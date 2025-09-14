package com.xiaozhi.demo.mvc;

import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.Controller;
import com.xiaozhi.demo.annotation.RequestMapping;
import com.xiaozhi.demo.annotation.ResponseBody;
import com.xiaozhi.demo.ioc.BeanPostProcess;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 调度器
 * 处理请求的流程在调度器中实现
 *
 * @author DD
 */
@Component
public class DispatchServlet extends HttpServlet implements BeanPostProcess {

    private Map<String, RequestHandler> handlerMap = new HashMap<>();

    private List<HandlerInterceptor> handlerInterceptorList = new ArrayList<>();

    @SneakyThrows
    @Override
    public void service(ServletRequest req, ServletResponse resp) {
        // 前置拦截

        // 获取处理当前请求的 handler
        RequestHandler requestHandler = findRequestHandler((HttpServletRequest) req);

        // 执行 handler 的处理方法
        Object returnValue = invokeRequestHandler(requestHandler);

        // 响应数据
        doReturnValue(returnValue, requestHandler);

        // 后置拦截

    }

    private void doReturnValue(Object returnValue, RequestHandler requestHandler) {
        RequestHandler.ResponseType responseType = requestHandler.getResponseType();
        switch (responseType) {
            case JSON -> {

            }
            case FILE -> {

            }
            case HTML -> {

            }
        }
    }

    private Object invokeRequestHandler(RequestHandler requestHandler) {
        Method method = requestHandler.getMethod();
        return null;
    }

    private RequestHandler findRequestHandler(HttpServletRequest req) {

        return handlerMap.get(req.getRequestURI());
    }

    /**
     * 转换所有 controller 的对象
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        handlerController(bean);

        handlerInterceptor(bean);
        return bean;
    }

    private void handlerController(Object bean) {
        Class<?> beanClass = bean.getClass();
        // 判断是非是 Controller 类型的 bean
        if (!beanClass.isAssignableFrom(Controller.class)) {
            return;
        }

        // 获取所有标有 @RequestMapping 注解的方法
        Arrays.stream(beanClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .forEach(m -> {
                    // 获取方法处理的路径
                    String handlerPath = getHandlerPath(beanClass, m);
                    if (this.handlerMap.containsKey(handlerPath)) {
                        throw new RuntimeException("存在相同路径的接口");
                    }

                    RequestHandler requestHandler = new RequestHandler().setMethod(m).setControllerBean(bean);
                    if (m.isAnnotationPresent(ResponseBody.class)) {
                        requestHandler.setResponseType(RequestHandler.ResponseType.JSON);
                    }

                    // 创建 RequestHandler
                    this.handlerMap.put(handlerPath, requestHandler);
                });
    }

    private String getHandlerPath(Class<?> beanClass, Method m) {
        // 获取类上的 @RequestMapping 注解信息
        RequestMapping classReqMp = beanClass.getAnnotation(RequestMapping.class);
        // 获取方法上的 @RequestMapping 注解信息
        RequestMapping methodReqMp = m.getAnnotation(RequestMapping.class);
        String path = "";
        if (classReqMp != null) {
            String classPath = classReqMp.value();
            if (StringUtils.isNotBlank(classPath)) {
                path += classPath;
            }
        }

        // TODO 这里还需要处理路径，这里就不处理了
        return path.concat(methodReqMp.value());
    }

    private void handlerInterceptor(Object bean) {

    }
}
