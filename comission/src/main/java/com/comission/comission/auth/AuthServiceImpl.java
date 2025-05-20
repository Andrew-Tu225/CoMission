package com.comission.comission.auth;

import com.comission.comission.DTO.RegisterRequest;
import com.comission.comission.client.Client;
import com.comission.comission.client.profile.event.ClientCreatedEvent;
import com.comission.comission.common.AppUser;
import com.comission.comission.common.AppUserRepository;
import com.comission.comission.user.profile.event.UserCreatedEvent;
import com.comission.comission.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuthServiceImpl(
            AppUserRepository appUserRepo, AuthenticationManager authManager, JWTService jwtService, ApplicationEventPublisher eventPublisher, BCryptPasswordEncoder passwordEncoder)
    {
        this.appUserRepo=appUserRepo;
        this.authManager=authManager;
        this.jwtService=jwtService;
        this.eventPublisher=eventPublisher;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser register(RegisterRequest request) {
        String registerType = request.getRegisterType().toUpperCase();
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        AppUser newUser = null;

        switch(registerType)
        {
            case "CLIENT":
                Client client = new Client();
                client.setUsername(request.getUsername());
                client.setEmail(request.getEmail());
                client.setPassword(encodedPassword);
                client.setClient(true);

                newUser = client;
                break;

            case "USER":
                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(encodedPassword);
                user.setUser(true);
                newUser=user;
                break;

            default:
                throw new IllegalArgumentException("Invalid register type: " + registerType);
        }

        publishUserCreatedEvent(newUser);
        appUserRepo.save(newUser);
        return newUser;

    }

    protected void publishUserCreatedEvent(AppUser newUser) {
        if(newUser.isUser())
        {
            eventPublisher.publishEvent(new UserCreatedEvent((User)newUser));
        }
        else if(newUser.isClient())
        {
            eventPublisher.publishEvent(new ClientCreatedEvent((Client)newUser));
        }
    }

    @Override
    public ResponseEntity<?> login(AppUser appUser) {
        try{
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword()));

            String accessToken = jwtService.generateToken(appUser.getUsername());
            String refreshToken = jwtService.generateRefreshToken(appUser.getUsername());

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        }
        catch(AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
