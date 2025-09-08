package com.xiaozhi.demo.thread.pool;

/**
 * 拒绝策略
 *
 * @author DD
 */
public interface RejectPolicy {

    void handler(Runnable runnable);
}
