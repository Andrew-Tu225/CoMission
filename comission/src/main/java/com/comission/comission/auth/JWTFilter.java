package com.comission.comission.auth;

import com.comission.comission.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserServiceImpl userService;

    @Autowired
    public JWTFilter(JWTService jwtService, UserServiceImpl userService)
    {
        this.jwtService = jwtService;
        this.userService=userService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/auth/login") || request.getServletPath().equals("auth/refresh-token")
        || request.getServletPath().equals("/auth/register"))
        {
            filterChain.doFilter(request,response);
        }
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails user = userService.loadUserByUsername(username);
            if(jwtService.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
