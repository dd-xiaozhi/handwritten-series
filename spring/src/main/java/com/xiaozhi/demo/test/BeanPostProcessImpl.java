package com.xiaozhi.demo.test;

import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.ioc.BeanPostProcess;

/**
 *
 * @author DD
 */
@Component
public class BeanPostProcessImpl implements BeanPostProcess {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前置处理...");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后置处理...");
        return bean;
    }
}
