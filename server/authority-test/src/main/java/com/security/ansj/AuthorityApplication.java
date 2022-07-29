package com.security.ansj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.security.ansj"
})
public class AuthorityApplication {
    public static void main(String[] args) {

        SpringApplication.run(AuthorityApplication.class, args);
    }
}
