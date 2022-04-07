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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    // Username
    private String username;

    // Password
    private String password;
}
