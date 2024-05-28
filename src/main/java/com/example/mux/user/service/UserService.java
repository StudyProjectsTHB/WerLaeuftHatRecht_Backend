package com.example.mux.user.service;

import com.example.mux.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.mux.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createNewUserIfNotExistByEmail(User user) {
        if (!userRepository.findByEmail(user.getUsername()).isPresent()) {
            userRepository.save(user);
        }
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
