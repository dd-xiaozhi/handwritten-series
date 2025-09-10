package com.xiaozhi.demo;

import com.xiaozhi.demo.ioc.ApplicationContext;
import lombok.SneakyThrows;

/**
 *
 * @author DD
 */
public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("com.xiaozhi.demo");
    }
}
