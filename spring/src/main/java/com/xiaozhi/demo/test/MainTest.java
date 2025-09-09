package com.xiaozhi.demo.test;

import com.xiaozhi.demo.ioc.ApplicationContext;

import java.util.List;

/**
 *
 * @author DD
 */
public class MainTest {

    public static void main(String[] args) {
        ApplicationContext ioc = new ApplicationContext("com.xiaozhi.demo");
        List<Animal> list = ioc.getBeans(Animal.class);
        System.out.println(list);
    }
}
