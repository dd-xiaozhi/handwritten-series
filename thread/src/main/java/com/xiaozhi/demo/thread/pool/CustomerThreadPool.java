package com.xiaozhi.demo.thread.pool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 *
 * @author DD
 */
@Data
@Accessors(chain = true)
public class CustomerThreadPool {

    /**
     * 核心线程数
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 最大线程数
     */
    private int maximumPoolSize = corePoolSize * 2;

    /**
     * 救急线程空闲时间
     */
    private long timout = 1;

    /**
     * 时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 任务队列容量
     */
    private int taskQueueCapacity = 10;

    /**
     * 任务队列
     */
    private BlockedQueue<Runnable> taskQueue = new BlockedQueue<>(taskQueueCapacity);

    /**
     * 核心线程列表
     */
    private List<Thread> coreThreadList = new ArrayList<>(corePoolSize);

    /**
     * 救急线程列表 (懒加载)
     */
    private List<Thread> emergencyThreadList = new ArrayList<>();

    /**
     * 拒绝策略
     */
    private RejectPolicy rejectPolicy;

    private ReentrantLock lock = new ReentrantLock();

    public void execute(Runnable task) {
        lock.lock();
        try {
            if (coreThreadList.size() < corePoolSize) {
                System.out.println("创建核心线程...");
                Thread worker = new Worker(task, true);
                coreThreadList.add(worker);
                worker.start();
            } else if (taskQueue.size() < taskQueueCapacity) {
                taskQueue.add(task);
            } else if (emergencyThreadList.size() + coreThreadList.size() < maximumPoolSize) {
                System.out.println("创建救急线程...");
                Thread worker = new Worker(task, false);
                emergencyThreadList.add(worker);
                worker.start();
            } else {
                throw new RuntimeException("队列已满...");
            }
        } catch (RuntimeException e) {
            rejectPolicy.handler(task);
        } finally {
            lock.unlock();
        }
    }

    @AllArgsConstructor
    private class Worker extends Thread {

        private Runnable task;
        private boolean isCore;

        @Override
        public void run() {
            Runnable task = this.task;
            while (task != null || (task = isCore
                    ? taskQueue.poll() : taskQueue.timoutPoll(timout, unit)) != null) {
                try {
                    task.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    task = null;
                }
            }

            // 移除救急线程
            emergencyThreadList.remove(this);
        }
    }
}
