package com.xiaozhi.demo.scheduled.task;

/**
 *
 * @author DD
 */
public class TestMain {

    public static void main(String[] args) throws InterruptedException {
        Scheduled scheduled = new Scheduled();
        scheduled.executor("test1",  () -> {
            System.out.println("100 毫秒任务");
        }, 100);
        scheduled.executor("test2", () -> {
            System.out.println("200 毫秒任务");
        }, 200);

        Thread.sleep(2000);
        scheduled.stop("test1");
    }
}
