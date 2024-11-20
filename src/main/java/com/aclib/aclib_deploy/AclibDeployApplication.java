package com.aclib.aclib_deploy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.aclib.aclib_deploy")
public class AclibDeployApplication {

    public static void main(String[] args) {
        SpringApplication.run(AclibDeployApplication.class, args);
    }

}
