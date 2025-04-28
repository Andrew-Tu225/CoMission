package com.comission.comission.service;

import com.comission.comission.model.User;
import com.comission.comission.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private AuthenticationManager authManager;
    private JWTService jwtService;

    @Autowired
    public UserService(
            UserRepository userRepository,@Lazy AuthenticationManager authManager,JWTService jwtService)
    {
        this.userRepository=userRepository;
        this.authManager=authManager;
        this.jwtService=jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isEmpty()){
            System.out.println("username not found");
            throw new UsernameNotFoundException("Username is not found");
        }
        else {
            return user.get();
        }
    }

    public User register(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String verify(User user)
    {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated())
        {
            return jwtService.generateToken(user.getUsername());
        }

        return "fail";
    }
}
