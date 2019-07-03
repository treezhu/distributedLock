package net.treezhu.lock.base;

/**
 * @author treezhu
 * @date 2019-07-02 11:57
 * @description
 */
public interface LockNameStategy {

    default String getLockName(String name) {
        return name;
    }
}
