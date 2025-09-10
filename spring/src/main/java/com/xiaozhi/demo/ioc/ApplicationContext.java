package com.xiaozhi.demo.ioc;

import com.xiaozhi.demo.annotation.Autowired;
import com.xiaozhi.demo.annotation.Component;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * IOC 容器
 *
 * @author DD
 */
public class ApplicationContext {

    private final Map<String, Object> ioc = new HashMap<>();

    // 正在加载创建的 Bean
    private final Map<String, Object> loadingIoc = new HashMap<>();

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final List<BeanPostProcess> beanPostProcessList = new ArrayList<>();

    /**
     * @param packagePath 包路径
     */
    public ApplicationContext(String packagePath) {
        initContext(packagePath);
    }

    /**
     * 初始化容器
     *
     * @param packagePath 包路径, 例如: com.test.xxxx
     */
    @SneakyThrows
    private void initContext(String packagePath) {
        // 扫描包，创建 BeanDefinition
        scanPackage(packagePath).stream().filter(this::scanCreate).forEach(this::wrapper);

        // 初始化 Bean 初始化处理器
        initBeanPostProcess(beanDefinitionMap);

        // 创建 Bean
        beanDefinitionMap.values().stream().forEach(this::createBean);
    }

    /**
     * 扫描包下所有的类
     * @param packageName 包名
     * @return List<Class < ?>>
     */
    @SneakyThrows
    protected List<Class<?>> scanPackage(String packageName) {
        ArrayList<Class<?>> packageClassList = new ArrayList<>();

        // 传入的是包名，需要转成路径
        String packagePath = packageName.replace(".", File.separator);
        URL resource = this.getClass().getClassLoader().getResource(packagePath);
        Files.walkFileTree(Path.of(resource.toURI()), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // 获取绝对路径
                String classFilePath = file.toAbsolutePath().toString();
                // 如果是 class 文件
                if (classFilePath.endsWith(".class")) {
                    // 转换成包类名的格式
                    String replaceStr = classFilePath.replace(File.separator, ".");
                    int packageIndex = replaceStr.indexOf(packageName);
                    String className = replaceStr.substring(packageIndex, replaceStr.length() - ".class".length());
                    try {
                        packageClassList.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return packageClassList;
    }

    private void initBeanPostProcess(Map<String, BeanDefinition> beanDefinitionMap) {
        beanDefinitionMap.values().forEach(bean -> {
            if (BeanPostProcess.class.isAssignableFrom(bean.getBeanType())) {
                beanPostProcessList.add((BeanPostProcess) createBean(bean));
            }
        });
    }

    protected boolean scanCreate(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(Component.class);
    }

    @SneakyThrows
    protected Object createBean(BeanDefinition beanDefinition) {
        String beanName = beanDefinition.getBeanName();
        if (ioc.containsKey(beanName)) {
            return ioc.get(beanName);
        }

        if (loadingIoc.containsKey(beanName)) {
            return loadingIoc.get(beanName);
        }
        return doCreateBean(beanDefinition);
    }

    @SneakyThrows
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Constructor<?> constructor = beanDefinition.getConstructor();
        // 创建对象
        Object bean = constructor.newInstance();
        loadingIoc.put(beanDefinition.getBeanName(), bean);

        // 自动注入
        doAutowiredBean(bean, beanDefinition);

        bean = initializedBean(bean, beanDefinition);

        loadingIoc.remove(beanDefinition.getBeanName());
        ioc.put(beanDefinition.getBeanName(), bean);
        return bean;
    }

    @SneakyThrows
    private Object initializedBean(Object bean, BeanDefinition beanDefinition) {
        // 责任链模式
        for (BeanPostProcess beanPostProcess : beanPostProcessList) {
            bean = beanPostProcess.postProcessBeforeInitialization(bean, beanDefinition.getBeanName());
        }

        // 调用初始化方法
        Method postInitMethod = beanDefinition.getPostInitMethod();
        if (postInitMethod != null) {
            postInitMethod.invoke(bean);
        }

        for (BeanPostProcess beanPostProcess : beanPostProcessList) {
            bean = beanPostProcess.postProcessAfterInitialization(bean, beanDefinition.getBeanName());
        }
        return bean;
    }

    @SneakyThrows
    private void doAutowiredBean(Object bean, BeanDefinition beanDefinition) {
        List<Field> autowiredFieldList = beanDefinition.getAutowiredFieldList();
        if (autowiredFieldList != null && !autowiredFieldList.isEmpty()) {
            for (Field field : autowiredFieldList) {
                String fieldClassName = field.getType().getName();
                boolean autowiredIsRequire = field.getAnnotation(Autowired.class).require();
                Object autowiredBean = null;
                if (!ioc.containsKey(fieldClassName)) {
                    if (autowiredIsRequire) {
                        BeanDefinition autowiredBeanDefinition = beanDefinitionMap.get(fieldClassName);
                        if (autowiredBeanDefinition == null) {
                            throw new RuntimeException("找不到这个类型的bean: " + fieldClassName);
                        }
                        // 判断在容器中是否已经存在，如果没有则需要创建 (循环依赖发生处)
                        autowiredBean = createBean(autowiredBeanDefinition);
                    }
                } else {
                    autowiredBean = ioc.get(fieldClassName);
                }

                if (autowiredBean != null) {
                    field.setAccessible(true);
                    field.set(bean, autowiredBean);
                }
            }
        }
    }

    private void wrapper(Class<?> beanClass) {
        BeanDefinition beanDefinition = new BeanDefinition(beanClass);
        beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
    }

    public Object getBean(String name) {
        return ioc.get(name);
    }

    public <T> T getBean(Class<T> beanType) {
        return getBeans(beanType).getFirst();
    }

    public <T> List<T> getBeans(Class<T> beanType) {
        if (Objects.nonNull(ioc.values())) {
            /**
             * Spring 通过 Bean 类型去获取 Bean 时为什么要遍历整个容器？
             * 我们是否可以把类型使用一个 Map 来存放，直接 map.get(beanType) 获取对应类型的 Bean
             * 这种方式是可以，但是有局限性，比如传入一个父类或者接口类型，此时就获取不到对应实现类的 Bean
             * 所以 Spring 只能遍历整个 IOC 容器获取对应类型的 Bean
             */
            return ioc.values().stream()
                    .filter(b -> beanType.isAssignableFrom(b.getClass()))
                    .map(b -> (T) b)
                    .toList();
        }

        return List.of();
    }
}
