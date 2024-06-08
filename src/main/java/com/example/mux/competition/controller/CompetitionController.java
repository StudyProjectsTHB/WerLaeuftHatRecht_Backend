package com.example.mux.competition.controller;

import com.example.mux.competition.model.Competition;
import com.example.mux.competition.service.CompetitionService;
import com.example.mux.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/competition")
@AllArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionService;

    @GetMapping
    public ResponseEntity<Competition> getCompetition(){
        try {
            return ResponseEntity.ok(competitionService.getCompetition());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping
    public ResponseEntity<Competition> updateCompetition(){
        try {
            return ResponseEntity.ok(competitionService.updateCompetition());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
