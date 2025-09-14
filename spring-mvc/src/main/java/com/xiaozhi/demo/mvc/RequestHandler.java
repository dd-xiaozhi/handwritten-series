package com.xiaozhi.demo.mvc;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;

/**
 *
 * @author DD
 */
@Data
@Accessors(chain = true)
public class RequestHandler {

    /**
     * 实际执行方法
     */
    private Method method;

    /**
     * 执行请求的 bean
     */
    private Object controllerBean;

    /**
     * 响应类型，支持 json，html，本地文件
     */
    private ResponseType responseType;

    enum ResponseType {
        JSON, HTML, FILE
    }
}
