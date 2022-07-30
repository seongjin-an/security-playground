package com.security.ansj.config;

import com.security.ansj.service.Paper;
import com.security.ansj.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final PaperService paperService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        Paper paper = paperService.getPaper((long) targetId);
        if(paper == null) throw new AccessDeniedException("시험지가 존재하지 않음");
        if(paper.getState() == Paper.State.PREPARE) return false;
        boolean canUse = paper.getStudentIds().stream().filter(userId -> userId.equals(authentication.getName())).findAny().isPresent();
        return canUse;
    }
}
