package net.treezhu.lock.autoconfig;

import net.treezhu.lock.annotation.DistributedLock;
import net.treezhu.lock.base.DistributedLockTemplate;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author treezhu
 * @date 2019-06-10 17:33
 * @description
 */
public class DistributedLockInterceptor implements MethodInterceptor {

    private DistributedLockTemplate template;

    public DistributedLockInterceptor(DistributedLockTemplate template) {
        this.template = template;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) {
        Method method = methodInvocation.getMethod();
        DistributedLock dl = method.getAnnotation(DistributedLock.class);
        String lockName = dl.lockName();
        if (StringUtils.isEmpty(lockName)) {
            lockName = method.getDeclaringClass().getName() + "." + method.getName();
        }
        boolean isLock;
        if (dl.tryLock()) {
            if (dl.release()) {
                isLock = template.tryLock(lockName, dl.waitTime(), dl.leaseTime(), dl.fairLock());
            } else {
                isLock = template.tryLock(lockName, dl.waitTime(), dl.fairLock());
            }
        } else {
            if (dl.release()) {
                template.lock(lockName, dl.leaseTime(), dl.fairLock());
            } else {
                template.lock(lockName, dl.fairLock());
            }
            isLock = true;
        }
        Object result = null;
        try {
            result = methodInvocation.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (isLock){
                template.unLock(lockName);
            }
        }
        return result;
    }


}
