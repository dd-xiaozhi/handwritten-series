package com.xiaozhi.demo.ioc;

import com.xiaozhi.demo.annotation.Component;
import lombok.SneakyThrows;

import java.io.File;
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

    /**
     * @param packagePath 包路径
     */
    public ApplicationContext(String packagePath) {
        initContext(packagePath);
    }

    /**
     * 初始化容器
     *
     * @param packagePath 包路径, eg: com.test.xxxx
     */
    @SneakyThrows
    private void initContext(String packagePath) {
        scanPackage(packagePath).stream()
                .filter(this::scanCreate)
                .map(this::wrapper)
                .forEach(bd -> ioc.put(bd.getBeanName(), this.createBean(bd)));
    }

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

    protected boolean scanCreate(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(Component.class);
    }

    @SneakyThrows
    protected Object createBean(BeanDefinition beanDefinition) {
        return beanDefinition.getConstructor().newInstance();
    }

    private BeanDefinition wrapper(Class<?> beanClass) {
        return new BeanDefinition(beanClass);
    }

    public Object getBean(String name) {
        return ioc.get(name);
    }

    public <T> T getBean(Class<T> beanType) {
        return getBeans(beanType).getFirst();
    }

    public <T> List<T> getBeans(Class<T> beanType) {
        if (Objects.nonNull(ioc.values())) {
            return ioc.values().stream()
                .filter(b -> beanType.isAssignableFrom(b.getClass()))
                .map(b -> (T) b)
                .toList();
        }

        return List.of();
    }
}
