package com.comission.comission.service;

import com.comission.comission.model.User;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    public User register(User user);
    public ResponseEntity<?> login(User user);
}
