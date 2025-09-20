package com.xiaozhi.demo.scheduled.task;

/**
 *
 * @author DD
 */
public class TestMain {

    public static void main(String[] args) {
        Scheduled scheduled = new Scheduled();
        scheduled.executor(() -> {
            System.out.println("100 毫秒任务");
        }, 100);
        scheduled.executor(() -> {
            System.out.println("200 毫秒任务");
        }, 200);
    }
}
