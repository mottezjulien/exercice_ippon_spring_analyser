package com.example.springanalyser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private User user;

    @GetMapping("/login/{userName}")
    public UserResponse value(@PathVariable("userName") String userName) {
        user.setUserName(userName);
        login();
        return welcomeMessage();
    }

    private void login() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private UserResponse welcomeMessage() {
        UserResponse response = new UserResponse();
        response.setMessage("Bienvenue " + user.getUserName());
        return response;
    }
}








