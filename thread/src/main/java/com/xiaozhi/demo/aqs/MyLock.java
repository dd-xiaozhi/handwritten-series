package com.xiaozhi.demo.aqs;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author DD
 */
public class MyLock {

    AtomicInteger state = new AtomicInteger(0);
    AtomicBoolean flag = new AtomicBoolean(false);
    Thread owner;
    // dump 节点方式的单链表
    AtomicReference<Node> head = new AtomicReference<>(new Node());
    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    public void lock() {
        // cas 尝试修改 flag，成功则是获取到锁
        // 这段逻辑是非公平锁与公平锁的差别，去掉就是公平锁，让当前线程根据链表的顺序去进行唤醒
        if (flag.compareAndSet(false, true)) {
            owner = Thread.currentThread();
            System.out.println("直接获取锁：" + owner.getName());
            return;
        }


        // 放入到链表中 (这里有修改对象的操作，所以需要 cas 操作保证原子性)
        Node currentNode = new Node();
        currentNode.thread = Thread.currentThread();
        // 尝试放入尾节点
        while (true) {
            Node tailNode = tail.get();
            if (tail.compareAndSet(tailNode, currentNode)) {
                // 成功将之前的尾节点变成现在尾节点的前节点
                System.out.println("加入队列中： " + currentNode.thread.getName());
                currentNode.pre = tailNode;
                tailNode.next = currentNode;
                break;
            }
        }

        /**
         * 在放入之前尝试自己唤醒自己
         * 这里主要是保证当前线程在放入链表之前尝试唤醒自己
         * 避免存在放入链表前，对应的唤醒当前线程的线程阻塞了，如果是公平锁的话就会造成全部线程阻塞的问题
         */
        while (true) {
            // 如果说当前头结点的下一个节点是当前节点，则需要再次尝试获取锁，防止唤醒线程已经执行 unpark
            if (head.get() == currentNode.pre && flag.compareAndSet(false, true)) {
                owner = currentNode.thread;
                head.set(currentNode);
                // 清理节点，防止内存泄露
                currentNode.pre.next = null;
                currentNode.pre = null;
                System.out.println("自己唤醒自己获取的锁：" + owner.getName());
                return;
            }
            // 阻塞当前没有获取锁的线程
            LockSupport.park();
        }
    }


    public void unlock() {
        if (owner != Thread.currentThread()) {
            throw new RuntimeException("当前线程没有获取锁");
        }

        Node headNode = head.get();
        Node unParkNode = headNode.next;
        // owner 是当前活动线程，不需要 cas
        flag.set(false);
        if (unParkNode != null) {
            System.out.println("释放锁：" + owner.getName());
            // 唤醒头结点线程
            LockSupport.unpark(unParkNode.thread);
        }
    }

    /**
     * 可重入锁是基于上面锁的修改
     */
    public void reentrantLock() {
        Thread currentThread = Thread.currentThread();
        if (state.get() <= 0) {
            // 尝试修改成 1 获取锁
            if (state.compareAndSet(0, 1)) {
                System.out.println("首次获取锁：" + currentThread.getName());
                owner = currentThread;
                return;
            }
        } else {
            if (owner == currentThread) {
                // 如果持有锁线程，state 加 1
                System.out.println("重入锁：" + currentThread.getName() + ", state: " + state.incrementAndGet());
                return;
            }
        }

        Node currentNode = new Node();
        currentNode.thread = Thread.currentThread();
        while (true) {
            Node tailNode = tail.get();
            if (tail.compareAndSet(tailNode, currentNode)) {
                System.out.println("加入队列中： " + currentNode.thread.getName());
                currentNode.pre = tailNode;
                tailNode.next = currentNode;
                break;
            }
        }

        while (true) {
            if (head.get() == currentNode.pre && state.compareAndSet(0, 1)) {
                owner = currentNode.thread;
                head.set(currentNode);
                currentNode.pre.next = null;
                currentNode.pre = null;
                System.out.println("自己唤醒自己获取的锁：" + owner.getName());
                return;
            }
            LockSupport.park();
        }
    }


    public void reentrantUnlock() {
        if (owner != Thread.currentThread()) {
            throw new RuntimeException("当前线程没有获取锁");
        }

        int i = state.get();
        if (i > 1) {
            state.set(i - 1);
            System.out.println("释放重入锁：" + owner.getName() + ", state: " + state.get());
            return;
        }
        if (i <= 0) {
            throw new RuntimeException("重入锁解锁异常...");
        }

        Node headNode = head.get();
        Node unParkNode = headNode.next;
        state.set(0);
        owner = null;
        if (unParkNode != null) {
            System.out.println("释放锁：" + Thread.currentThread().getName());
            LockSupport.unpark(unParkNode.thread);
        }
    }

    private class Node {
        Thread thread;
        Node pre;
        Node next;
    }
}
