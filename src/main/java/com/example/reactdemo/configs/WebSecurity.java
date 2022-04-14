package com.example.reactdemo.configs;

import static com.example.reactdemo.configs.SecurityConstants.LOGIN_URL;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.reactdemo.filters.AuthTokenFilter;
import com.example.reactdemo.services.impl.UserServiceImpl;;

/**
 * @author binhtn1
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserServiceImpl userDetailsService;
    private AuthEntryPointJwt unauthorizedHandler;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthTokenFilter authTokenFilter;

    /**
     * Injection dependencies
     *
     * @param userDetailsService
     * @param unauthorizedHandler
     * @param bCryptPasswordEncoder
     * @param authTokenFilter
     */
    public WebSecurity(UserServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler, BCryptPasswordEncoder bCryptPasswordEncoder, AuthTokenFilter authTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authTokenFilter = authTokenFilter;
    }

    /**
     * Create bean AuthenticationManager
     *
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Config filter, request authenticated,...
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                // Handle exception authentication
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // Disabled session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(LOGIN_URL).permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Config password encoder for spring security
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Create bean config cors when developing different domain between FE and BE
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

