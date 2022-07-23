package com.security.ansj.teacher;

import com.security.ansj.student.Student;
import com.security.ansj.student.StudentAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {//인증토큰을 발행하는 프로바이더다.

    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
//        if(teacherDB.containsKey(token.getName())){
        if(teacherDB.containsKey(token.getCredentials())){
//            Teacher teacher = teacherDB.get(token.getName());
            Teacher teacher = teacherDB.get(token.getCredentials());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;//authenticated가 false로 할 경우 authentication을 핸들링했다는 것이 되어 문제가 되니 처리할 수 없는 것은 null로 넘긴다.
    }

    @Override
    public boolean supports(Class<?> authentication) {//매니저에서 허용하는 토큰
//        return authentication == UsernamePasswordAuthenticationToken.class;
        return authentication == TeacherAuthenticationToken.class;
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
        new HashSet<Teacher>(Arrays.asList(
                new Teacher("choi", "최선생", new HashSet(Arrays.asList(new SimpleGrantedAuthority("ROLE_TEACHER"))))
        )).forEach(s -> teacherDB.put(s.getId(), s));
    }
}
