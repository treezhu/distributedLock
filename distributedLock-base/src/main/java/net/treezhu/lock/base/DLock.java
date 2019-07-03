package net.treezhu.lock.base;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author treezhu
 * @date 2019-06-06 11:49
 * @description
 */
public interface DLock extends Lock {

    /**
     * 获取指定期限的锁的方法
     *
     * @param leaseTime
     * @param unit
     */
    void lock(long leaseTime, TimeUnit unit);


    /**
     * 尝试获取指定期限的锁的方法
     *
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     */
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit);

    /**
     * 可阻断指定期限的锁的方法
     *
     * @param leaseTime
     * @param unit
     * @throws InterruptedException
     */
    void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException;
}
