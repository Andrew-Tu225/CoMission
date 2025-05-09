package com.comission.comission.auth;

import com.comission.comission.user.User;
import com.comission.comission.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authServiceImpl;
    private final JWTService jwtService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AuthController(AuthServiceImpl authServiceImpl,JWTService jwtService, UserServiceImpl userServiceImpl)
    {
        this.authServiceImpl=authServiceImpl;
        this.jwtService = jwtService;
        this.userServiceImpl=userServiceImpl;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody User user)
    {
        return authServiceImpl.login(user);
    }

    @PostMapping("register")
    public User register(@RequestBody User user)
    {
        return authServiceImpl.register(user);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response)
    {
        String authHeader = request.getHeader("Authorization");
        String refreshToken = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            try{
                refreshToken = authHeader.substring(7);
                username = jwtService.extractUsername(refreshToken);
                UserDetails user = userServiceImpl.loadUserByUsername(username);

                if(jwtService.validateToken(refreshToken, user)) {
                    String accessToken = jwtService.generateToken(user.getUsername());
                    Map<String, Object> tokens = new HashMap<>();
                    tokens.put("accessToken", accessToken);
                    tokens.put("refreshToken", refreshToken);

                    return ResponseEntity.ok(tokens);
                }
                else {
                    throw new RuntimeException("Invalid refresh token");
                }
            }
            //handle exception later
            catch(Exception e)
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
            }
        }
        else
        {
            throw new RuntimeException("refresh token is missing");
        }
    }


}
