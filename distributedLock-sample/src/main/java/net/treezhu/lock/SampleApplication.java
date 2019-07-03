package net.treezhu.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author treezhu
 * @date 2019-06-11 11:13
 * @description
 */
@SpringBootApplication
public class SampleApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
