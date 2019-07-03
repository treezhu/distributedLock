package net.treezhu.lock.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author treezhu
 * @date 2019-06-14 17:18
 * @description
 */
@ConfigurationProperties(prefix = "spring.dlock")
public class DistributedLockProperties {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
