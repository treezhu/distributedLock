package net.treezhu.lock.support.redis;

import net.treezhu.lock.base.DLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * @author treezhu
 * @date 2019-06-10 18:05
 * @description
 */
public class RedisLock implements DLock, Serializable {

    private JedisPool jedisPool;
    private String lockName;
    private Sync sync;

    interface Sync {

    }

    static final class NonfairSync implements Sync {

    }

    static final class FairSync implements Sync {

    }

    public RedisLock(JedisPool jedisPool, String lockName) {
        this.jedisPool = jedisPool;
        this.lockName = lockName;
        this.sync = new NonfairSync();
    }

    public RedisLock(JedisPool jedisPool, String lockName, boolean fair) {
        this.jedisPool = jedisPool;
        this.lockName = lockName;
        this.sync = fair ? new FairSync() : new NonfairSync();
    }

    @Override
    public void lock(long leaseTime, TimeUnit unit) {
        try {
            lockInterruptibly(leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) {
        long startTime = System.currentTimeMillis();
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = Collections.singletonList(lockName);
            List<String> args = Collections.singletonList(String.valueOf(Thread.currentThread().getId()));
            boolean isLock = false;
            while (!isLock) {
                if (System.currentTimeMillis() - startTime >= waitTime) {
                    return false;
                }
                isLock = evalLockScript(jedis, keys, args);
            }
            jedis.expire(lockName, (int) unit.toSeconds(leaseTime));
            return true;
        }
    }

    @Override
    public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = Collections.singletonList(lockName);
            List<String> args = Collections.singletonList(String.valueOf(Thread.currentThread().getId()));
            boolean isLock = false;
            while (!isLock) {
                isLock = evalLockScript(jedis, keys, args);
                ;
            }
            jedis.expire(lockName, (int) unit.toSeconds(leaseTime));
        }
    }

    @Override
    public void lock() {
        try {
            lockInterruptibly();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = Collections.singletonList(lockName);
            List<String> args = Collections.singletonList(String.valueOf(Thread.currentThread().getId()));
            boolean isLock = false;
            while (!isLock) {
                isLock = evalLockScript(jedis, keys, args);
            }
        }
    }

    @Override
    public boolean tryLock() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = Collections.singletonList(lockName);
            List<String> args = Collections.singletonList(String.valueOf(Thread.currentThread().getId()));
            return evalLockScript(jedis, keys, args);
        }
    }

    @Override
    public boolean tryLock(long waitTime, TimeUnit unit) {
        long startTime = System.currentTimeMillis();
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = Collections.singletonList(lockName);
            List<String> args = Collections.singletonList(String.valueOf(Thread.currentThread().getId()));
            boolean isLock = false;
            while (!isLock) {
                if (System.currentTimeMillis() - startTime >= waitTime) {
                    return false;
                }
                isLock = evalLockScript(jedis, keys, args);
            }
            return true;
        }
    }

    @Override
    public void unlock() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = Collections.singletonList(lockName);
            List<String> args = Collections.singletonList(String.valueOf(Thread.currentThread().getId()));
            evalUnLockScript(jedis, keys, args);
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }


    private final static String LOCK_SCRIPT = "if(redis.call('exists',KEYS[1])==0) then\n" +
            "\tredis.call('hset',KEYS[1],'onwer',ARGV[1])\n" +
            "\tredis.call('hset',KEYS[1],'state','1')\n" +
            "\treturn 1;\n" +
            "else\n" +
            "\tif(redis.call('hget',KEYS[1],'onwer') == ARGV[1]) then\n" +
            "\t\tredis.call('hincrby',KEYS[1],'state','1')\n" +
            "\t\treturn 1;\n" +
            "\telse\n" +
            "\t\treturn 0;\n" +
            "\tend\n" +
            "end";

    public static final String UNLOCK_SCRIPT = "if(redis.call('hget',KEYS[1],'onwer') == ARGV[1]) then\n" +
            "\t\tif(redis.call('hincrby',KEYS[1],'state','-1')==0) then\n" +
            "\t\t\tredis.call('del',KEYS[1]);\n" +
            "\t\tend;"+
            "\t\treturn 1;\n" +
            "\telse\n" +
            "\t\treturn 0;\n" +
            "\tend";

    private boolean evalLockScript(Jedis jedis, List<String> keys, List<String> args) {
        Long value = (Long) jedis.eval(LOCK_SCRIPT, keys, args);
        return value == 1;
    }

    private boolean evalUnLockScript(Jedis jedis, List<String> keys, List<String> args) {
        Long value = (Long) jedis.eval(UNLOCK_SCRIPT, keys, args);
        return value == 1;
    }

}
