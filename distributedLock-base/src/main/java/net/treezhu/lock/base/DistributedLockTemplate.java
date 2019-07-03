package net.treezhu.lock.base;

/**
 * @author treezhu
 * @date 2019-06-10 17:09
 * @description
 */
public interface DistributedLockTemplate {

    /**
     * 使用分布式锁。自定义锁的超时时间
     *
     * @param lockName  锁名
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param fairLock  是否使用公平锁
     */
    void lock(String lockName,long leaseTime, boolean fairLock);

    /**
     * 使用分布式锁,锁无超时时间
     * @param lockName 锁名
     * @param fairLock 是否使用公平锁
     */
    void lock(String lockName, boolean fairLock);

    /**
     * 尝试分布式锁，自定义等待时间、超时时间。
     *
     * @param lockName  锁名
     * @param waitTime  获取锁最长等待时间
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param fairLock  是否使用公平锁
     * @return 是否获取了锁
     */
    boolean tryLock(String lockName,long waitTime, long leaseTime, boolean fairLock);

    /**
     * 尝试分布式锁，自定义等待时间, 无超时时间
     *
     * @param lockName  锁名
     * @param waitTime  获取锁最长等待时间
     * @param fairLock  是否使用公平锁
     */
    boolean tryLock(String lockName,long waitTime,boolean fairLock);

    /**
     * 解锁
     * @param lockName
     */
    void unLock(String lockName);
}
