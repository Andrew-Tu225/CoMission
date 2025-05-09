package com.comission.comission.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String getPage()
    {
        return "hello";
    }

    @PostMapping("/register")
    public String register()
    {
        return "register page";
    }

    @PostMapping("/login")
    public String login()
    {
        return "login page";
    }

}