package com.xiaozhi.demo.test;

import com.xiaozhi.demo.annotation.Autowired;
import com.xiaozhi.demo.annotation.Component;

/**
 *
 * @author DD
 */
@Component
public class Cat implements Animal {

    @Autowired
    private Dog dog;
}
