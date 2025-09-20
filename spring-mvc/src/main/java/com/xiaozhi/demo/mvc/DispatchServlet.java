package com.xiaozhi.demo.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.ClassPath;
import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.Controller;
import com.xiaozhi.demo.annotation.RequestMapping;
import com.xiaozhi.demo.annotation.ResponseBody;
import com.xiaozhi.demo.ioc.BeanPostProcess;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.apache.commons.lang3.ClassPathUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final String FORWARD_TAG = "forward:";

    @SneakyThrows
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 获取处理当前请求的 handler
            RequestHandler requestHandler = findRequestHandler(req);

            // 前置拦截
            doHandlerInterceptor(true, requestHandler, req, resp);

            // 执行 handler 的处理方法
            Object returnValue = invokeRequestHandler(requestHandler, req, resp);

            // 响应数据
            doReturnValue(returnValue, requestHandler, req, resp);

            // 后置拦截
            doHandlerInterceptor(false, requestHandler, req, resp);

        } catch (Exception ex) {
            // 处理异常，这里直接报 500
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @SneakyThrows
    private void doReturnValue(Object returnValue,
                               RequestHandler requestHandler,
                               HttpServletRequest req,
                               HttpServletResponse resp) {
        if (returnValue == null) {
            return;
        }

        RequestHandler.ResponseType responseType = requestHandler.getResponseType();
        ServletOutputStream outputStream = resp.getOutputStream();
        switch (responseType) {
            case JSON -> {
                resp.setContentType("application/json;chatSet=utf-8");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writer().writeValue(outputStream, returnValue);
            }
            case FILE -> {

            }
            case HTML -> {
                if (returnValue instanceof String str && str.startsWith(FORWARD_TAG)) {
                    String htmlFileName = str.substring(FORWARD_TAG.length());
                    resp.setContentType("text/html");
                    resp.setCharacterEncoding("utf-8");;
                    String htmlStr = RenderingTemplate(htmlFileName, req);
                    outputStream.write(htmlStr.getBytes(StandardCharsets.UTF_8));
                }
            }
            default -> outputStream.write(returnValue.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    @SneakyThrows
    private String RenderingTemplate(String htmlFileName, HttpServletRequest req) {
        InputStream htmlResource = this.getClass().getClassLoader()
                            .getResourceAsStream(htmlFileName + ".html");
        String htmlStr = new String(htmlResource.readAllBytes());
        // 提取出 {{ 变量 }}
        String regex = "\\{\\{(.*?)\\}\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlStr);
        while (matcher.find()) {
            String variableName = matcher.group(1);
            // 这里直接从请求域中获取
            String variableValue = req.getAttribute(variableName).toString();
            htmlStr = htmlStr.replace("{{" + variableName + "}}", variableValue);
        }
        return htmlStr;
    }

    @SneakyThrows
    private Object invokeRequestHandler(RequestHandler requestHandler,
                                        HttpServletRequest req,
                                        HttpServletResponse resp) {
        Method method = requestHandler.getMethod();

        Object[] args = getMethodParameters(method, req, resp);

        return method.invoke(requestHandler.getControllerBean(), args);
    }

    // 设置方法参数

    private Object[] getMethodParameters(Method method,
                                         HttpServletRequest req,
                                         HttpServletResponse resp) {
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            // 这里会有很多的 converter 处理器处理请求参数（这里简单处理）
            if (parameter.getType().equals(HttpServletRequest.class)) {
                return new Object[]{req};
            }
        }
        return null;
    }

    private RequestHandler findRequestHandler(HttpServletRequest req) {
        RequestHandler requestHandler = handlerMap.get(req.getRequestURI());
        if (requestHandler == null) {
            throw new RuntimeException("找不到这个请求对应的处理器");
        }
        return requestHandler;
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
        if (!beanClass.isAnnotationPresent(Controller.class)) {
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
                    requestHandler.setResponseType(getResponseType(m));

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

    private RequestHandler.ResponseType getResponseType(Method method) {
        if (method.isAnnotationPresent(ResponseBody.class)) {
            return RequestHandler.ResponseType.JSON;
        } else if (method.getReturnType().isAssignableFrom(String.class)) {
            return RequestHandler.ResponseType.HTML;
        }
        return null;
    }

    private void handlerInterceptor(Object bean) {
        if (bean instanceof HandlerInterceptor handlerInterceptor) {
            handlerInterceptorList.add(handlerInterceptor);
        }
    }

    private void doHandlerInterceptor(boolean isPreHandle,
                                      RequestHandler requestHandler,
                                      HttpServletRequest req,
                                      HttpServletResponse resp) {
        handlerInterceptorList.forEach(handler -> {
            try {
                if (isPreHandle) {
                    if (!handler.preHandle(req, resp, requestHandler)) {
                        throw new RuntimeException("请求被拦截....");
                    }
                } else {
                    handler.postHandle(req, resp, requestHandler);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
