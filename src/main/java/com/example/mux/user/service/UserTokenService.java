package com.example.mux.user.service;

import com.example.mux.user.UserProperties;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import com.example.mux.user.repository.UserTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserTokenService {
    private final UserTokenRepository userTokenRepository;

    private final UserProperties userProperties;

    public UserToken buildUserToken(User user){
        return new UserToken(user, LocalDateTime.now().plus(Duration.ofHours(userProperties.getExpiresAfter())));
    }

    public List<UserToken> saveUserTokens(List<UserToken> userTokens){
        return userTokenRepository.saveAll(userTokens);
    }

    public UserToken saveUserToken (UserToken userToken){
        return userTokenRepository.save(userToken);
    }

    public UserToken getUserToken(UUID uuid) throws EntityNotFoundException {
        return userTokenRepository.findByToken(uuid).orElseThrow(() -> new EntityNotFoundException("Token with this uuid not found."));
    }
}
