package com.comission.comission.auth;

import com.comission.comission.user.User;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    public User register(User user);
    public ResponseEntity<?> login(User user);
}
