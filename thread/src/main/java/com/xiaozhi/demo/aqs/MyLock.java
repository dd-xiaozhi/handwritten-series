package com.xiaozhi.demo.aqs;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author DD
 */
public class MyLock {

    AtomicBoolean flag = new AtomicBoolean(false);
    Thread owner;
    // dump 节点方式的单链表
    AtomicReference<Node> head = new AtomicReference<>(new Node());
    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    public void lock() {
        // cas 尝试修改 flag，成功则是获取到锁
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


    private class Node {
        Thread thread;
        Node pre;
        Node next;
    }
}
