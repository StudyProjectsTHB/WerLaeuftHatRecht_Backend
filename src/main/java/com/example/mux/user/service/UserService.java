package com.example.mux.user.service;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.user.UserProperties;
import com.example.mux.user.exception.PasswordMismatchException;
import com.example.mux.user.exception.TokenExpiredException;
import com.example.mux.user.exception.TokenNotFoundException;
import com.example.mux.user.model.User;
import com.example.mux.user.model.dto.AuthenticationRequestDTO;
import com.example.mux.user.model.dto.AuthenticationResponseDTO;
import com.example.mux.user.model.dto.UserCreationDTO;
import com.example.mux.user.model.dto.UserPasswordsDTO;
import com.example.mux.user.model.UserToken;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.example.mux.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTokenService userTokenService;

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
}
