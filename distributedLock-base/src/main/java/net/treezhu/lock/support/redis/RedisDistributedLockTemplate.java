package net.treezhu.lock.support.redis;

import net.treezhu.lock.base.DLock;
import net.treezhu.lock.base.DistributedLockTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author treezhu
 * @date 2019-06-11 11:16
 * @description
 */
public class RedisDistributedLockTemplate implements DistributedLockTemplate {

    private RedisDLockFactory redisDLockFactory;

    public RedisDistributedLockTemplate(RedisDLockFactory redisDLockFactory) {
        this.redisDLockFactory = redisDLockFactory;
    }

    @Override
    public void lock(String lockName, long leaseTime, boolean fairLock) {
        DLock lock = getLock(lockName, fairLock);
        lock.lock(leaseTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void lock(String lockName, boolean fairLock) {
        DLock lock = getLock(lockName, fairLock);
        lock.lock();
    }

    @Override
    public boolean tryLock(String lockName, long waitTime, long leaseTime, boolean fairLock) {
        DLock lock = getLock(lockName, fairLock);
        return lock.tryLock(waitTime,leaseTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryLock(String lockName, long waitTime, boolean fairLock) {
        DLock lock = getLock(lockName, fairLock);
        try {
            return lock.tryLock(waitTime,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private DLock getLock(String name, boolean fairLock) {
        DLock dLock;
        if (fairLock) {
            dLock = redisDLockFactory.getFairLock(name);
        } else {
            dLock = redisDLockFactory.getLock(name);
        }
        return dLock;
    }

    @Override
    public void unLock(String lockName) {
        DLock lock = getLock(lockName,false);
        lock.unlock();
    }
}
