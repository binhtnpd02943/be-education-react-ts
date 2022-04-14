package com.example.reactdemo.controlles;

import com.example.reactdemo.dtos.LoginRequest;
import com.example.reactdemo.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.reactdemo.configs.SecurityConstants.HEADER_STRING;

/**
 * @author binhtn1
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    /**
     * Injection dependencies
     *
     * @param authService
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Handle login
     *
     * @param loginRequest
     * @return ResponseEntity with data is a JwtResponse
     */
    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Handle simple refresh token
     *
     * @param header
     * @return ResponseEntity with data is a JwtResponse
     */
    @Operation(summary = "Mã token làm mới")
    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader(HEADER_STRING) String header) {
        return authService.refreshToken(header);
    }
}
