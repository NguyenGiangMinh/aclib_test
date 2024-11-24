package com.aclib.aclib_deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.aclib.aclib_deploy")
@EnableScheduling
public class AclibDeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(AclibDeployApplication.class, args);
    }

}
