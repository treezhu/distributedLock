package net.treezhu.lock.base;

/**
 * @author  treezhu
 */
public abstract class AbstractDLockFactory {

    /**
     * 获取锁操作对象
     *
     * @param name
     * @return
     */
    public abstract DLock getLock(String name);

    /**
     * 获取公平锁操作对象
     *
     * @param name
     * @return
     */
    public abstract DLock getFairLock(String name);
}
