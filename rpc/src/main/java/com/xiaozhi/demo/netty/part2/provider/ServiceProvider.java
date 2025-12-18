package com.xiaozhi.demo.netty.part2.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供者，通过接口名获取对应的服务类实例
 *
 * @author DD
 */
public class ServiceProvider {

    private static final Map<String, Object> SERVICE_BEAN_MAP = new ConcurrentHashMap<>();

    public void addService(Object serviceBean) {
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            SERVICE_BEAN_MAP.put(anInterface.getName(), serviceBean);
        }
    }

    public Object getService(String interfaceName) {
        Object serviceBean = SERVICE_BEAN_MAP.get(interfaceName);
        if (serviceBean == null) {
            throw new RuntimeException("未找到服务接口：" + interfaceName);
        }
        return serviceBean;
    }
}
