package com.example.reactdemo.configs;

/**
 * @author binhtn1
 */
public class SecurityConstants {
    // Secret key of json web token
    public static final String SECRET = "MySecretKey";

    // Expiration time of json web token
    public static final long EXPIRATION_TIME = 172_800_000;

    // Type of token
    public static final String TOKEN_PREFIX = "Bearer ";

    // Header name take json web token
    public static final String HEADER_STRING = "Authorization";

    // Url login
    public static final String LOGIN_URL = "/auth/login";
}
