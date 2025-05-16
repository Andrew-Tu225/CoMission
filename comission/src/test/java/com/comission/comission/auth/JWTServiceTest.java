package com.comission.comission.auth;

import com.comission.comission.common.AppUserService;
import com.comission.comission.user.Role;
import com.comission.comission.user.User;
import com.comission.comission.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {
    @Mock
    private AppUserService appUserService;
    @InjectMocks
    private JWTService jwtService;
    private User testUser;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @BeforeEach
    public void setup()
    {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(List.of(new Role("PLATFORM_USER")));
        testUser = user;
    }

    @Test
    void checkUsernameMatchedInGenerateTokenTest() {
        Mockito.when(appUserService.loadUserByUsername(testUser.getUsername())).thenReturn(testUser);
        String jwtToken = jwtService.generateToken(testUser.getUsername());

        Assertions.assertFalse(jwtToken.isBlank());
        //test the username the same as in jwtToken
        Assertions.assertEquals(testUser.getUsername(), jwtService.extractUsername(jwtToken));
    }

    @Test
    void checkUsernameMatchedInGenerateRefreshTokenTest() {
        String refreshToken = jwtService.generateRefreshToken(testUser.getUsername());

        Assertions.assertFalse(refreshToken.isBlank());
        //test the username the same as in refreshToken
        Assertions.assertEquals(testUser.getUsername(), jwtService.extractUsername(refreshToken));
    }
}