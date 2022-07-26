package com.security.ansj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.security.ansj.config"
})
public class RemembermeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemembermeApplication.class, args);
    }
}
