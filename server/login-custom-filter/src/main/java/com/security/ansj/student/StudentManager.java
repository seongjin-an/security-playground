package com.security.ansj.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {//인증토큰을 발행하는 프로바이더다.

    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if(studentDB.containsKey(token.getName())){
            Student student = studentDB.get(token.getName());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;//authenticated가 false로 할 경우 authentication을 핸들링했다는 것이 되어 문제가 되니 처리할 수 없는 것은 null로 넘긴다.
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        Set.of(
//                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
//                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
//                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")))
//        ).forEach(s->
//                studentDB.put(s.getId(), s)
//        );
        new HashSet<Student>(Arrays.asList(
                new Student("hong", "홍길동", new HashSet(Arrays.asList(new SimpleGrantedAuthority("ROLE_STUDENT")))),
                new Student("kang", "강아지", new HashSet(Arrays.asList(new SimpleGrantedAuthority("ROLE_STUDENT")))),
                new Student("rang", "호랑이", new HashSet(Arrays.asList(new SimpleGrantedAuthority("ROLE_STUDENT"))))
        )).forEach(s -> studentDB.put(s.getId(), s));
    }
}
