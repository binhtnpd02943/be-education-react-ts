package com.example.reactdemo.services;

import com.example.reactdemo.dtos.LoginRequest;
import org.springframework.http.ResponseEntity;

/**
 * 
 * @author binhtn1
 *
 */
public interface AuthService {

    /**
     * Handle login
     *
     * @param loginRequest
     * @return ResponseEntity with data is a JwtResponse
     */
    ResponseEntity<?> login(LoginRequest loginRequest);

    /**
     * Handle simple refresh token
     *
     * @param header
     * @return ResponseEntity with data is a JwtResponse
     */
    ResponseEntity<?> refreshToken(String header);
}
