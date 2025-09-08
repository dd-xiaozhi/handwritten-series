package com.xiaozhi.demo.thread.pool;

/**
 *
 * @author DD
 */
public class Test {

    public static void main(String[] args) {
        CustomerThreadPool pool = new CustomerThreadPool()
                .setCorePoolSize(1)
                .setMaximumPoolSize(2)
                .setTaskQueueCapacity(1)
                .setRejectPolicy(new RejectPolicyImpl());
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            pool.execute(() -> {
                System.out.println("正在处理：" + finalI);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
