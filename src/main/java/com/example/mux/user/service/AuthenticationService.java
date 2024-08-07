package com.example.mux.user.service;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.service.GroupService;
import com.example.mux.user.UserProperties;
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
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JWTManagerService jwtManagerService;
    private final UserTokenService userTokenService;
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final EmailService emailService;
    private final UserProperties userProperties;
    private final UserService userService;

    public AuthenticationResponseDTO authenticateUser(AuthenticationRequestDTO request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtManagerService.generateJWT(user);

        return new AuthenticationResponseDTO(accessToken, user);
    }

    public User registerUser(UserPasswordsDTO userPasswords, String token) throws TokenNotFoundException, TokenExpiredException, PasswordMismatchException {
        try {
            User user = userService.getUserFromToken(userPasswords, token, userTokenService);
            user.setStepGoal(userProperties.getInitStepGoal());
            user.setPassword(userPasswords.getPassword());

            return userRepository.save(user);
        } catch (EntityNotFoundException e) {
            throw new TokenNotFoundException(e);
        }
    }

    public List<UserToken> createUsers(List<UserCreationDTO> userCreations) throws EntityNotFoundException {
        List<User> users = new LinkedList<>();
        List<UserToken> userTokens = new LinkedList<>();

        for(UserCreationDTO userCreation: userCreations){
            User user = new User(userCreation.getEmail(), userCreation.isAdmin());
            try {
                Group group = groupService.getGroup(userCreation.getGroupId());
                user.setGroup(group);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Group with given id does not exists.");
            }
            Pair<String, String> competitionName = AvailableNameService.getAvailableName();
            user.setAdjective(competitionName.getFirst());
            user.setNoun(competitionName.getSecond());
            users.add(user);

            userTokens.add(userTokenService.buildUserToken(user));

        }

        userRepository.saveAll(users);
        userTokenService.saveUserTokens(userTokens);
        emailService.sendWelcomeEmails(users, userTokens);

        return userTokens;
    }
}
