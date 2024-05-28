package com.example.mux;

import com.example.mux.user.model.User;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MyStartupRunner implements CommandLineRunner {
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("test@test.test");
        user.setPassword("testtest");
        user.setNoun("Tiger");
        user.setAdjective("smart");
        userService.createNewUserIfNotExistByEmail(user);
    }
}
