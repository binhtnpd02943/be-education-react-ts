package com.example.reactdemo.services.impl;

import com.example.reactdemo.apiresponse.ResponseCustom;
import com.example.reactdemo.dtos.JwtResponse;
import com.example.reactdemo.dtos.LoginRequest;
import com.example.reactdemo.models.ApplicationUser;
import com.example.reactdemo.services.AuthService;
import com.example.reactdemo.services.UserService;
import com.example.reactdemo.utils.JwtUtils;
import com.example.reactdemo.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.example.reactdemo.configs.SecurityConstants.EXPIRATION_TIME;
import static com.example.reactdemo.configs.SecurityConstants.TOKEN_PREFIX;

/**
 * 
 * @author binhtn1
 *
 */
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    private UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    /**
     * Handle login
     *
     * @param loginRequest
     * @return ResponseEntity with data is a JwtResponse
     */
    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList()).get(0);

        ResponseCustom<Object> response = new ResponseCustom<Object>(HttpStatus.OK);
        response.setData(new JwtResponse(TOKEN_PREFIX + jwt, userDetails.getUsername(), EXPIRATION_TIME, role));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Handle simple refresh token
     *
     * @param header
     * @return ResponseEntity with data is a JwtResponse
     */
    @Override
    public ResponseEntity<?> refreshToken(String header) {
        String username = jwtUtils.getUsernameFromBearerToken(header);

        ResponseCustom<Object> response = new ResponseCustom<>(HttpStatus.OK, MessageUtil.getMessage("Auth.refreshSuccess"));

        ApplicationUser user = userService.findByUsername(username);

        String token = jwtUtils.refreshToken(username);
        response.setData(new JwtResponse(TOKEN_PREFIX + token, username, EXPIRATION_TIME, user.getRole().getName().name()));
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
