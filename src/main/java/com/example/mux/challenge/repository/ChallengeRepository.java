package com.example.mux.challenge.repository;

import com.example.mux.challenge.model.Challenge;
import com.example.mux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    List<Challenge> findAllByUsers(Set<User> users);
}
