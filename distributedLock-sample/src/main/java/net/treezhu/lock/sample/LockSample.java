package net.treezhu.lock.sample;

import net.treezhu.lock.annotation.DistributedLock;
import org.springframework.stereotype.Component;

/**
 * @author treezhu
 * @date 2019-06-11 11:09
 * @description
 */
@Component
public class LockSample {

    @DistributedLock(release = false, tryLock = true)
    public void test() {
        System.out.println("lock test");
    }

    @DistributedLock(lockName = "reentrantLock", release = false)
    public void reentrantLockTest() {
        System.out.println("LockSample");
    }

}
