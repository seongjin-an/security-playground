package com.security.ansj.config;

import com.security.ansj.domain.SpUser;
import com.security.ansj.service.SpUserService;
import org.springframework.beans.factory.InitializingBean;

public class DbInit implements InitializingBean {
    private final SpUserService userService;

    public DbInit(SpUserService userService) {
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!userService.findUser("user1").isPresent()){
            SpUser user = userService.save(
                    SpUser.builder()
                            .email("user1")
                            .password("1111")
                            .enabled(true)
                            .build()
            );
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if(!userService.findUser("user2").isPresent()){
            SpUser user = userService.save(
                    SpUser.builder()
                            .email("user2")
                            .password("1111")
                            .enabled(true)
                            .build()
            );
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if(!userService.findUser("admin").isPresent()){
            SpUser user = userService.save(
                    SpUser.builder()
                            .email("admin")
                            .password("1111")
                            .enabled(true)
                            .build()
            );
            userService.addAuthority(user.getUserId(), "ROLE_ADMIN");
        }
    }
}
