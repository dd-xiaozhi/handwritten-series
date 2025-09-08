package com.xiaozhi.demo.thread.pool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 *
 * @author DD
 */
public class BlockedQueue<E> {

    public BlockedQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 容量
     */
    private int capacity;

    /**
     * 队列
     */
    private Queue<E> queue = new ArrayDeque<>(capacity);

    private final ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 生产者条件
     */
    private final Condition productCondition = reentrantLock.newCondition();

    /**
     * 消费者条件
     */
    private final Condition customerCondition = reentrantLock.newCondition();

    public int size() {
        return queue.size();
    }

    public void add(E e) {
        reentrantLock.lock();
        try {
            // 判断队列是否已满，满了则不给添加
            while (queue.size() >= capacity) {
                System.out.println("队列已满...");
                try {
                    productCondition.await();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

            queue.add(e);
            System.out.println("添加元素: " + e);
            // 唤醒消费线程
            customerCondition.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean timoutAdd(E e, long timeout, TimeUnit unit) {
        reentrantLock.lock();
        try {
            // 判断队列是否已满，满了则不给添加
            while (queue.size() >= capacity) {
                System.out.println("队列已满...");
                try {
                    // 超时等待
                    long nanos = unit.toNanos(timeout);
                    if (nanos <= 0) {
                        return false;
                    }

                    nanos = productCondition.awaitNanos(nanos);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            queue.add(e);
            System.out.println("添加元素: " + e);
            // 唤醒消费线程
            customerCondition.signal();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E poll() {
        reentrantLock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println("队列已空...");
                try {
                    customerCondition.await();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

            E e = queue.poll();
            System.out.println("消费元素: " + e);
            productCondition.signal();
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E timoutPoll(long timeout, TimeUnit unit) {
        reentrantLock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                System.out.println("队列已空...");
                try {
                    if (nanos <= 0) {
                        return null;
                    }
                    System.out.println("超时等待...");
                    nanos = customerCondition.awaitNanos(nanos);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

            E e = queue.poll();
            System.out.println("消费元素: " + e);
            productCondition.signal();
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }
}
