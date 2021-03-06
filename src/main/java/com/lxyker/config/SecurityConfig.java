package com.lxyker.config;

import com.lxyker.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    final LoginFailureHandler loginFailureHandler;
    final LoginSuccessHandler loginSuccessHandler;
    final MyLogoutSuccessHandler logoutSuccessHandler;
    final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(LoginFailureHandler loginFailureHandler, LoginSuccessHandler loginSuccessHandler, MyLogoutSuccessHandler logoutSuccessHandler, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, UserDetailsServiceImpl userDetailsService) {
        this.loginFailureHandler = loginFailureHandler;
        this.loginSuccessHandler = loginSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
    };

    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()

//                ????????????
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)

//                ????????????
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)

//                ??????session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                ??????????????????
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()

//                ??????????????????
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

//                ????????????????????????
                .and()
                .addFilter(jwtAuthenticationFilter())
        ;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
