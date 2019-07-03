package net.treezhu.lock.annotation;

import java.lang.annotation.*;

/**
 * @author treezhu
 * @date 2019-06-10 17:15
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    /**
     * 锁名 ,默认为：类名.方法名
     *
     * @return
     */
    String lockName() default "";

    /**
     * 自动释放锁
     *
     * @return
     */
    boolean release() default true;

    /**
     * 是否使用公平锁。
     * 公平锁即先来先得。
     */
    boolean fairLock() default false;

    /**
     * 是否使用尝试锁。
     */
    boolean tryLock() default false;

    /**
     * 最长等待时间。 该字段只有当tryLock()返回true才有效。
     */
    long waitTime() default 5*1000L;

    /**
     * 锁超时时间。
     * 超时时间过后，锁自动释放。
     */
    long leaseTime() default 30*1000L;
}
