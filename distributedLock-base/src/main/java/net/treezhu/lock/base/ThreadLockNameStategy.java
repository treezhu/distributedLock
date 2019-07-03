package net.treezhu.lock.base;

/**
 * @author treezhu
 * @date 2019-07-02 11:58
 * @description
 */
public class ThreadLockNameStategy implements LockNameStategy {

    private final static String THREAD_PREFIX = "Thread-";

    @Override
    public String getLockName(String name) {
        Thread thread = Thread.currentThread();
        return THREAD_PREFIX + thread.getId() + "-" + name;
    }
}
