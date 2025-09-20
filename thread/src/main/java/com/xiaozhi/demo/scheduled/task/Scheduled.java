package com.xiaozhi.demo.scheduled.task;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private final Map<String, Job> jobMap = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void executor(String jobName, Runnable task, long delay) {
        if (jobMap.containsKey(jobName)) {
            throw new RuntimeException("任务名不能重复！！！");
        }

        Job job = new Job(jobName, task, delay);
        jobMap.put(jobName, job);
        trigger.taskQueue.offer(job);
        trigger.wakeUp();
    }

    public void stop(String jobName) {
        // 从任务容器中移除
        Job job = jobMap.remove(jobName);
        Thread currentThread = job.getCurrentThread();
        // 获取当前执行任务的线程
        if (currentThread != null && Thread.State.RUNNABLE.equals(currentThread.getState())) {
            System.out.println("任务" + jobName + "已暂停");
            currentThread.interrupt();
        }
    }


    private class Trigger {

        private final Thread triggerThread;
        private final PriorityBlockingQueue<Job> taskQueue = new PriorityBlockingQueue();

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
                    if (jobMap.containsKey(job.getJobName())) {
                        executorService.execute(job);
                        // 创建下一轮时间的任务到队列中
                        job.setStartTime(job.startTime + job.getDelay());
                        taskQueue.offer(job);
                    }
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
    private static class Job implements Runnable, Comparable<Job> {

        private String jobName;
        private long delay;
        // 当前执行任务的线程
        private Thread currentThread;
        private Runnable task;
        private long startTime;

        public Job(String jobName, Runnable task, long delay) {
            this.task = task;
            this.delay = delay;
            this.jobName = jobName;
            this.startTime = System.currentTimeMillis() + delay;
        }

        @Override
        public int compareTo(Job o) {
            return (int) (this.startTime - o.startTime);
        }

        @Override
        public void run() {
            this.currentThread = Thread.currentThread();
            task.run();
        }
    }
}
