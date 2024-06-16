package com.example.mux.user.service;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.mux.user.repository.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with this email not found."));
    }

    public User getUser(int ID) throws EntityNotFoundException {
        return userRepository.findById(ID).orElseThrow(() -> new EntityNotFoundException("User with this ID not found."));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(int ID) {
        userRepository.deleteById(ID);
    }

    public boolean userExists(String email){
        return userRepository.existsByEmail(email);
    }

    public List<User> getUsers(Group group){
        return userRepository.findAllByGroup(group);
    }

    public User createAndRegisterIfNotExist(User user){
        Optional<User> userOptional = userRepository.getUserByEmail(user.getEmail());
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            return userRepository.save(user);
        }
    }
}
