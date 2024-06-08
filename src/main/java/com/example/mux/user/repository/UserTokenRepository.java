package com.example.mux.user.repository;

import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTokenRepository extends JpaRepository<UserToken, UUID> {
    Optional<UserToken> findByToken(UUID uuid);
}
