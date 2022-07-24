package com.security.ansj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.security.ansj"
})
@EntityScan(basePackages = {
        "com.security.ansj.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.security.ansj.repository"
})
public class UserDetailsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserDetailsApplication.class, args);
    }
}
