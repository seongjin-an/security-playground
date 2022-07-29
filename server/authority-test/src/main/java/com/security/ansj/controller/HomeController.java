package com.security.ansj.controller;

import com.security.ansj.service.SecurityMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final SecurityMessageService securityMessageService;

    MethodSecurityInterceptor interceptor;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/greeting/{name}")
    public String greeting(@PathVariable String name){
        return "hello " + securityMessageService.message(name);
    }

}
