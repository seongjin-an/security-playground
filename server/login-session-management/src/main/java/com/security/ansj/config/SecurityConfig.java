package com.security.ansj.config;

import com.security.ansj.service.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    SecurityContextPersistenceFilter securityContextPersistenceFilter;//세션에서 시큐리티 컨텍스트를 보관
    RememberMeAuthenticationFilter rememberMeAuthenticationFilter;//세션이 만료되면 세션에서 시큐리티 컨텍스트를 찾지 않고 여기서 찾음
    TokenBasedRememberMeServices tokenBasedRememberMeServices;
    PersistentRememberMeToken persistentRememberMeToken;

    private final SpUserService userService;
    private final DataSource dataSource;

    public SecurityConfig(SpUserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        SessionRegistryImpl registry = new SessionRegistryImpl();
        return registry;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(request->
                        request.antMatchers("/").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(login ->
                        login.loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/", false)
                                .failureUrl("/login-error")
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                )
                .exceptionHandling(exception ->
//                        exception.accessDeniedPage("/access-denied")
                        exception.accessDeniedHandler(new CustomDeniedHandler())
                                .authenticationEntryPoint(new CustomEntryPoint())
                )
//                .rememberMe()
                .rememberMe(r -> r.rememberMeServices(rememberMeServices()))
                .sessionManagement(
                        s -> s
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .sessionFixation(sessionFixationConfigurer ->
//                                        sessionFixationConfigurer.none()
                                        sessionFixationConfigurer.changeSessionId()//로그인 시 세션 아이디가 바뀜
                                )
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false)//true 시 maxSession보다 많은 세션에서 로그인이 안됨
                                .expiredUrl("/session-expired")
                )
                ;
    }

    @Bean
    PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try{
            repository.removeUserTokens("1");
        }catch(Exception ex){
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices(){
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("hello", userService, tokenRepository());
        service.setAlwaysRemember(true);
        return service;
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                System.out.printf("====>>[%s] 세션 생성됨 %s \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                System.out.printf("====>>[%s] 세션 만료됨 %s \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                super.sessionIdChanged(event, oldSessionId);
                System.out.printf("====>>[%s] 세션 아이디 변경 %s:%s \n", LocalDateTime.now(), oldSessionId, event.getSession().getId());
            }
        });
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console()
                )
                .antMatchers("/sessions", "/session/expire", "/session-expired")
                .antMatchers("/favicon.ico", "/error")

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser(User.builder().username("user2").password(passwordEncoder().encode("2222")).roles("USER"))
//                .withUser(User.builder().username("admin").password(passwordEncoder().encode("3333")).roles("ADMIN"));
        auth.userDetailsService(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}
