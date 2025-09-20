package com.xiaozhi.demo.scheduled.task;

import java.util.concurrent.Executors;

/**
 *
 * @author DD
 */
public class TestMain {

    public static void main(String[] args) throws InterruptedException {
        Scheduled scheduled = new Scheduled(Executors.newFixedThreadPool(4));
        scheduled.executor("test1",  () -> {
            System.out.println("100 毫秒任务");
        }, 100);
        scheduled.executor("test2", () -> {
            System.out.println("200 毫秒任务");
        }, 200);

        Thread.sleep(1300);
        scheduled.stop("test1");
    }
}
