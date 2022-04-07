package com.example.reactdemo;

import com.example.reactdemo.filters.AuthTokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * ReactDemoApplication
 */
@SpringBootApplication
public class ReactDemoApplication {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactDemoApplication.class, args);
    }

}
