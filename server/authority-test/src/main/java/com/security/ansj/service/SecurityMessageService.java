package com.security.ansj.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecurityMessageService {
    @PreAuthorize("hasRole('USER')")
    public String message(String name){
        return name;
    }
}
