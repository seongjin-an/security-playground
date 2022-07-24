package com.security.ansj.config;

import com.security.ansj.student.StudentManager;
import com.security.ansj.teacher.TeacherManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(2)
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    public SecurityConfig(StudentManager studentManager, TeacherManager teacherManager) {
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager);
        auth.authenticationProvider(teacherManager);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .authorizeRequests(request ->
//                        request.anyRequest().permitAll()
                                request.antMatchers("/").permitAll()
                                        .anyRequest().permitAll()
                )
//                .formLogin(login ->
//                        login.loginPage("/login").permitAll()
//                                .defaultSuccessUrl("/", false)
//                                .failureUrl("/login-error")
//                )
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/favicon.ico", "/error");
    }
}
