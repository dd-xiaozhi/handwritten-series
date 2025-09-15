package com.xiaozhi.demo.test;

import com.xiaozhi.demo.ioc.ApplicationContext;
import lombok.SneakyThrows;

/**
 *
 * @author DD
 */
public class MainTest {

    @SneakyThrows
    public static void main(String[] args) {
        new ApplicationContext("com.xiaozhi.demo");
    }
}
