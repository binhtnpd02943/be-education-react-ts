package com.example.reactdemo.filters;

import com.example.reactdemo.services.impl.UserServiceImpl;
import com.example.reactdemo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.example.reactdemo.configs.SecurityConstants.*;

/**
 * 
 * @author binhtn1
 *
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    /**
     * Handle check jwt
     * @param req
     * @param res
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(req);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = userService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(req, res);
    }

    /**
     * Parse from header bearer token to json web token
     * @param req
     * @return String jwt
     */
    private String parseJwt (HttpServletRequest req){
        String headerAuth = req.getHeader(HEADER_STRING);
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)){
            return headerAuth.substring(7);
        }
        return null;
    }

}
