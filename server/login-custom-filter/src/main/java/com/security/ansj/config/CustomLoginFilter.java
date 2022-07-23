package com.security.ansj.config;

import com.security.ansj.student.StudentAuthenticationToken;
import com.security.ansj.teacher.TeacherAuthenticationToken;
import lombok.CustomLog;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    public CustomLoginFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.trim();
        String password = obtainPassword(request);
        password = (password != null) ? password : "";

        String type = request.getParameter("type");//type을 이용하여 각각의 authenticationToken을 달리할 수 있다.
        if(type == null || type.equals("student")){
            //student
//            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,password);
//            return this.getAuthenticationManager().authenticate(authRequest);
            StudentAuthenticationToken token = StudentAuthenticationToken.builder().credentials(username).build();
            return this.getAuthenticationManager().authenticate(token);
        }else{
            //teacher
//            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
//            return this.getAuthenticationManager().authenticate(authRequest);
            TeacherAuthenticationToken token = TeacherAuthenticationToken.builder().credentials(username).build();
            return this.getAuthenticationManager().authenticate(token);
        }
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
//        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
