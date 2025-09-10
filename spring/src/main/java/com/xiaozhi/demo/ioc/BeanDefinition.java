package com.xiaozhi.demo.ioc;

import com.xiaozhi.demo.annotation.Autowired;
import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.PostConstruct;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Bean 档案馆
 *
 * @author DD
 */
@Data
public class BeanDefinition {

    private String beanName;

    private Class<?> beanType;

    /**
     * 无参构造函数
     */
    private Constructor<?> constructor;

    /**
     * `@PostConstruct` 标记的初始化方法，会在对象创建完成后调用
     * 可以有多个初始化方法，这里我们让它只有一个
     */
    private Method postInitMethod;

    /**
     * 自动注入的属性
     */
    private List<Field> autowiredFieldList;

    @SneakyThrows
    public BeanDefinition(Class<?> beanClass) {
        String name = beanClass.getDeclaredAnnotation(Component.class).name();
        this.beanName = StringUtils.isNotBlank(name) ? name : beanClass.getName();
        this.beanType = beanClass;
        this.constructor = beanClass.getConstructor();
        this.postInitMethod = Arrays.asList(beanClass.getMethods()).stream()
                .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                .findFirst()
                .orElse(null);
        this.autowiredFieldList = Arrays.stream(beanClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Autowired.class)).toList();
    }

}
