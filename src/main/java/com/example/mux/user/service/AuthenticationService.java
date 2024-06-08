package com.example.mux.user.service;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.user.exception.PasswordMismatchException;
import com.example.mux.user.exception.TokenExpiredException;
import com.example.mux.user.exception.TokenNotFoundException;
import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import com.example.mux.user.model.dto.AuthenticationRequestDTO;
import com.example.mux.user.model.dto.AuthenticationResponseDTO;
import com.example.mux.user.model.dto.UserCreationDTO;
import com.example.mux.user.model.dto.UserPasswordsDTO;
import com.example.mux.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JWTManagerService jwtManagerService;
    private final UserTokenService userTokenService;
    private final UserRepository userRepository;

    public AuthenticationResponseDTO authenticateUser(AuthenticationRequestDTO request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtManagerService.generateJWT(user);
        AuthenticationResponseDTO response = new AuthenticationResponseDTO(accessToken);

        return response;
    }

    public User registerUser(UserPasswordsDTO userPasswords, String token) throws TokenNotFoundException, TokenExpiredException, PasswordMismatchException {
        try {
            UUID uuid = UUID.fromString(token);
            UserToken userToken = userTokenService.getUserToken(uuid);

            if (LocalDateTime.now().isAfter(userToken.getExpiresAt())) {
                throw new TokenExpiredException("The token is expired.");
            }

            if (!userPasswords.getPassword().equals(userPasswords.getPasswordConfirm())) {
                throw new PasswordMismatchException("The passwords do not match.");
            }

            User user = userToken.getUser();
            user.setPassword(userPasswords.getPassword());

            return userRepository.save(user);
        } catch (EntityNotFoundException e) {
            throw new TokenNotFoundException(e);
        }
    }

    public List<UserToken> createUsers(List<UserCreationDTO> userCreations) {
        List<User> users = new LinkedList<>();
        List<UserToken> userTokens = new LinkedList<>();

        userCreations.forEach(userCreation -> {
            User user = new User(userCreation.getEmail(), userCreation.isAdmin());
            user.setAdjective("kleiner");
            //TODO add Group from group id
            user.setNoun("Iltis");//TODO set random noun and adjective
            users.add(user);

            userTokens.add(userTokenService.buildUserToken(user));

        });

        userRepository.saveAll(users);
        userTokenService.saveUserTokens(userTokens);//TODO send emails to all users

        return userTokens;
    }
}
