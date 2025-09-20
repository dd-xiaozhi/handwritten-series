package com.xiaozhi.demo.scheduled.task;

import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author DD
 */
public class Scheduled {

    private final Trigger trigger = new Trigger();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void executor(Runnable task, long delay) {
        Job job = new Job(task, delay);
        trigger.taskQueue.offer(job);
        trigger.wakeUp();
    }


    private class Trigger {

        private final PriorityBlockingQueue<Job> taskQueue = new PriorityBlockingQueue();
        private final Thread triggerThread;

        public Trigger() {
            triggerThread = new Thread(this::initTriggerThread);
            triggerThread.start();
        }

        private void initTriggerThread() {
            while (true) {
                // while 防止线程虚假唤醒
                while (taskQueue.isEmpty()) {
                    LockSupport.park();
                }

                Job job = taskQueue.peek();
                if (job.getStartTime() <= System.currentTimeMillis()) {
                    job = taskQueue.poll();
                    executorService.execute(job.getTask());
                    // 创建下一轮时间的任务到队列中
                    job.setStartTime(job.startTime + job.getDelay());
                    taskQueue.offer(job);
                } else {
                    LockSupport.parkUntil(job.getStartTime());
                }
            }
        }


        /**
         * 唤醒线程
         */
        private void wakeUp() {
            LockSupport.unpark(triggerThread);
        }

    }


    @Data
    private static class Job implements Comparable<Job> {

        private Runnable task;
        private long delay;
        private long startTime;

        public Job(Runnable task, long delay) {
            this.task = task;
            this.delay = delay;
            this.startTime = System.currentTimeMillis() + delay;
        }

        @Override
        public int compareTo(Job o) {
            return (int) (this.startTime - o.startTime);
        }
    }
}
