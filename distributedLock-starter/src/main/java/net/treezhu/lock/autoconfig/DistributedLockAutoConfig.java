package net.treezhu.lock.autoconfig;

import net.treezhu.lock.annotation.DistributedLock;
import net.treezhu.lock.base.AbstractDLockFactory;
import net.treezhu.lock.base.DistributedLockTemplate;
import net.treezhu.lock.base.LockNameStategy;
import net.treezhu.lock.support.redis.RedisDLockFactory;
import net.treezhu.lock.support.redis.RedisDistributedLockTemplate;
import org.aopalliance.aop.Advice;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

/**
 * @author treezhu
 * @date 2019-06-10 17:24
 * @description
 */
@Configuration
@EnableConfigurationProperties({DistributedLockProperties.class, RedisProperties.class})
public class DistributedLockAutoConfig extends AbstractPointcutAdvisor {

    private Pointcut pointcut;

    private Advice advice;

    @Autowired
    private DistributedLockTemplate distributedLockTemplate;

    @PostConstruct
    public void init() {
        this.pointcut = new AnnotationMatchingPointcut(null, DistributedLock.class);
        this.advice = new DistributedLockInterceptor(distributedLockTemplate);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Bean
    @ConditionalOnMissingBean(JedisPool.class)
    public JedisPool jedisPool(RedisProperties redisProperties) {
        int timeOut = 10000;
        if (redisProperties.getTimeout() != null) {
            timeOut = (int) redisProperties.getTimeout().toMillis();
        }
        return new JedisPool(new GenericObjectPoolConfig(), redisProperties.getHost(), redisProperties.getPort(), timeOut, redisProperties.getPassword(), redisProperties.getDatabase());
    }


    @Bean
    @ConditionalOnMissingBean(AbstractDLockFactory.class)
    public RedisDLockFactory redisDLockFactory(JedisPool jedisPool) {
        return new RedisDLockFactory(jedisPool, new LockNameStategy(){});
    }

    @Bean
    @ConditionalOnMissingBean(DistributedLockTemplate.class)
    @ConditionalOnProperty(prefix = "spring.dlock", name = "type", havingValue = "redis")
    public DistributedLockTemplate redisDistributedLockTemplate(RedisDLockFactory redisDLockFactory) {
        return new RedisDistributedLockTemplate(redisDLockFactory);
    }
}
