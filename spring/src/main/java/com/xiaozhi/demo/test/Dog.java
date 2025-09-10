package com.xiaozhi.demo.test;

import com.xiaozhi.demo.annotation.Autowired;
import com.xiaozhi.demo.annotation.Component;
import com.xiaozhi.demo.annotation.PostConstruct;

import java.util.Random;

/**
 *
 * @author DD
 */
@Component
public class Dog implements Animal {

    @Autowired
    private Cat cat;

    @Autowired(require = false)
    private Random random;

    @PostConstruct
    public void inti() {
        System.out.println("我是小狗...");
        System.out.println("cat: " + cat);
    }
}
