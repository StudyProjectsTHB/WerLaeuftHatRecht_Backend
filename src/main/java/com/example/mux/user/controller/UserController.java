package com.example.mux.user.controller;

import com.example.mux.user.model.User;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
