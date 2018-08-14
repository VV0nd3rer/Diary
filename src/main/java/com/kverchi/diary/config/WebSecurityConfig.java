package com.kverchi.diary.config;

import com.kverchi.diary.security.AuthFailureHandler;
import com.kverchi.diary.security.AuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by Kverchi on 14.8.2018.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                  .antMatchers("/posts/save-post").authenticated()
                  .antMatchers("/posts/**").permitAll()
                  .antMatchers("/sights/map").permitAll()
                  //.antMatchers("/sights/**").authenticated()
                  .antMatchers("/users/login*").permitAll()
                //.antMatchers("/", "/home").permitAll()
                 // .anyRequest().authenticated()
                .and()
                .formLogin()
                  .loginPage("/users/login")
                  .failureHandler(failureHandler())
                  .successHandler(successHandler())
                  .permitAll()
                .and()
                .logout()
                  .logoutSuccessUrl("/sights/map")
                  .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/denied")
                .and().rememberMe()
                .and().csrf().disable();
    }

    @Bean
    public AuthSuccessHandler successHandler() {
        AuthSuccessHandler successHandler = new AuthSuccessHandler();
        return successHandler;
    }

    @Bean
    public AuthFailureHandler failureHandler() {
        AuthFailureHandler failureHandler = new AuthFailureHandler();
        return failureHandler;
    }

    @Bean
    public CookieCsrfTokenRepository tokenRepository() {
        CookieCsrfTokenRepository tokenRepository = new CookieCsrfTokenRepository();
        tokenRepository.setCookieHttpOnly(false);
        return tokenRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }
}
