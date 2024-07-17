package com.example.mux.challenge.repository;

import com.example.mux.challenge.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    List<Challenge> findAllByStartDateLessThanEqual(LocalDate date);
    List<Challenge> findAllByEndDateGreaterThanEqual(LocalDate date);
}
