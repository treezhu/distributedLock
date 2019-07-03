package net.treezhu.lock.base;

/**
 * 分布式锁回调接口
 *
 * @author treezhu
 */
@FunctionalInterface
public interface DistributedLockCallback<T> {

    T process();
}
