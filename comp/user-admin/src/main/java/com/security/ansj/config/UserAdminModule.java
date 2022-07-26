package com.security.ansj.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.security.ansj")
@EntityScan(basePackages = {
        "com.security.ansj.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.security.ansj.repository"
})
public class UserAdminModule {
}
