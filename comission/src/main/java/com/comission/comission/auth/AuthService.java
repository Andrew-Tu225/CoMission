package com.comission.comission.auth;

import com.comission.comission.DTO.RegisterRequest;
import com.comission.comission.common.AppUser;
import com.comission.comission.user.User;
import org.springframework.http.ResponseEntity;


public interface AuthService {
    public AppUser register(RegisterRequest request);
    public ResponseEntity<?> login(AppUser appUser);
}
