package com.example.mux.competition.repository;

import com.example.mux.competition.model.Competition;
import com.example.mux.day.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {
}
