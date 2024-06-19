package com.example.mux.challenge.repository;

import com.example.mux.challenge.model.ChallengeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeTypeRepository  extends JpaRepository<ChallengeType, Integer> {
}
