package com.xiaozhi.demo.thread.pool;

/**
 *
 * @author DD
 */
public class RejectPolicyImpl  implements RejectPolicy{
    @Override
    public void handler(Runnable runnable) {
        System.out.println("执行拒绝策略....");
    }
}
