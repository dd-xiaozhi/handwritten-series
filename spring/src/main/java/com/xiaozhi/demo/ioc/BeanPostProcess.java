package com.xiaozhi.demo.ioc;

/**
 *
 * @author DD
 */
public interface BeanPostProcess {

    /**
     * Bean 初始化前处理
     * @param bean
     * @param beanName
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * Bean 初始化后处理
     * @param bean
     * @param beanName
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
