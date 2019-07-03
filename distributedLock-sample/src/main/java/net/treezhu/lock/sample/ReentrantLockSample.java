package net.treezhu.lock.sample;

import net.treezhu.lock.annotation.DistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author treezhu
 * @date 2019-07-02 17:58
 * @description
 */
@Service
public class ReentrantLockSample {

    @Autowired
    private LockSample lockSample;

    @DistributedLock(lockName = "reentrantLock",release = false)
    public void reentrantLockTest(){
        lockSample.reentrantLockTest();
        System.out.println("ReentrantLockSample");
    }
}
