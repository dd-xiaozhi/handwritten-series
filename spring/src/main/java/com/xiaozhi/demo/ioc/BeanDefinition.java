package com.xiaozhi.demo.ioc;

import com.xiaozhi.demo.annotation.Component;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;

/**
 * Bean 档案馆
 *
 * @author DD
 */
@Data
public class BeanDefinition {

    private String beanName;

    /**
     * 无参构造函数
     */
    private Constructor<?> constructor;

    @SneakyThrows
    public BeanDefinition(Class<?> beanClass) {
        String name = beanClass.getDeclaredAnnotation(Component.class).name();
        this.beanName = StringUtils.isNotBlank(name) ? name : beanClass.getName();
        this.constructor = beanClass.getConstructor();
    }
}
