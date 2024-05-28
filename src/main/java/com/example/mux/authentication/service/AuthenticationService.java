package com.example.mux.authentication.service;

import com.example.mux.authentication.model.AuthenticationRequest;
import com.example.mux.authentication.model.AuthenticationResponse;
import com.example.mux.security.JwtTokenUtil;
import com.example.mux.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticateUser(AuthenticationRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtTokenUtil.generateAccessToken(user);
        AuthenticationResponse response = new AuthenticationResponse(accessToken);

        return response;
    }
}
