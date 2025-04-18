package com.comission.comission.controller;

import com.comission.comission.model.User;
import com.comission.comission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService)
    {
        this.userService=userService;
    }

    @GetMapping("/")
    public String getPage()
    {
        return "hello";
    }

    public UserDetails getUser(@RequestBody String username)
    {
        return userService.loadUserByUsername(username);
    }

}