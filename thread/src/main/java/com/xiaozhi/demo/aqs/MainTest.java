package com.xiaozhi.demo.aqs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DD
 * 基于 AQS（AbstractQueuedSynchronizer）实现 lock
 * AQS 解决的问题是保证并发数据安全以及减少CPU无意义的空转
 * 当前这里实现的是 AQS 的简化版
 */
public class MainTest {

    public static void main(String[] args) {
        int[] count = new int[]{10000};
        List<Thread> threads = new ArrayList<>();
        MyLock lock = new MyLock();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(() -> {
                try {
                    lock.lock();
                    for (int j = 0; j < 100; j++) {
                        count[0]--;
                    }
                } finally {
                    lock.unlock();
                }
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(count[0]);
    }
}
