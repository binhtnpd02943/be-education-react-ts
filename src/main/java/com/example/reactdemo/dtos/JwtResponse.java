package com.example.reactdemo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author binhtn1
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    // Token of user
    private String token;

    // User name
    private String username;

    // Expiration time of token
    private Long expirationTime;

    // Role of user
    private String role;
}
