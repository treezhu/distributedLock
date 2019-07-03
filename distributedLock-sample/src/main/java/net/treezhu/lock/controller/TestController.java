package net.treezhu.lock.controller;

import net.treezhu.lock.sample.LockSample;
import net.treezhu.lock.sample.ReentrantLockSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author treezhu
 * @date 2019-06-14 14:45
 * @description
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private LockSample lockSample;

    @Autowired
    private ReentrantLockSample reentrantLockSample;


    @RequestMapping("lock")
    public String testLock(){
        lockSample.test();
        return "success";
    }

    @RequestMapping("reentrantLock")
    public String testReentrantLock(){
        reentrantLockSample.reentrantLockTest();
        return "success";
    }
}
