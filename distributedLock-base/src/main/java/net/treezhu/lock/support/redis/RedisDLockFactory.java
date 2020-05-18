package net.treezhu.lock.support.redis;

import net.treezhu.lock.base.AbstractDLockFactory;
import net.treezhu.lock.base.DLock;
import net.treezhu.lock.base.LockNameStategy;
import redis.clients.jedis.JedisPool;

/**
 * @author treezhu
 * @date 2019-06-14 17:44
 * @description
 */
public class RedisDLockFactory extends AbstractDLockFactory {
    private final JedisPool jedisPool;
    private final LockNameStategy lockNameStategy;

    public RedisDLockFactory(JedisPool jedisPool,LockNameStategy lockNameStategy) {
        this.jedisPool = jedisPool;
        this.lockNameStategy = lockNameStategy;
    }

    @Override
    public DLock getLock(String name) {
        String lockName = lockNameStategy.getLockName(name);
        return new RedisLock(jedisPool,lockName);
    }

    @Override
    public DLock getFairLock(String name) {
        throw new UnsupportedOperationException();
    }
}
