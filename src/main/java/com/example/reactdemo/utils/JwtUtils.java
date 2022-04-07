package com.example.reactdemo.utils;

import com.example.reactdemo.services.impl.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.reactdemo.configs.SecurityConstants.EXPIRATION_TIME;
import static com.example.reactdemo.configs.SecurityConstants.SECRET;

/**
 * 
 * @author binhtn1
 *
 */

@Component
public class JwtUtils {

    /**
     * Generate json web token from authentication object
     *
     * @param authentication
     * @return jwt
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * Generate jwt from user name
     *
     * @param username
     * @return jwt
     */
    public String refreshToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * Get user name from json web token
     *
     * @param token
     * @return user name
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get user name from authorization header
     *
     * @param token
     * @return user name
     */
    public String getUsernameFromBearerToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.substring(7)).getBody().getSubject();
    }

    /**
     * Validate jwt
     *
     * @param authToken
     * @return boolean
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
