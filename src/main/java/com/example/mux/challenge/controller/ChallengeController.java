package com.example.mux.challenge.controller;

import com.example.mux.challenge.model.dto.ChallengeDTO;
import com.example.mux.challenge.service.ChallengeService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/challenges")
@AllArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserService userService;

    @GetMapping("/{date}")
    public ResponseEntity<List<ChallengeDTO>> getChallenges(@PathVariable LocalDate date, @AuthenticationPrincipal UserDetails userDetail){
        try {
            List<ChallengeDTO> userChallenges = challengeService.getUserChallenges(userService.getUser(userDetail.getUsername()),date);
            return ResponseEntity.ok(userChallenges);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Gets all (past and future) successful completed challenges of a user sorted by endDate
    @GetMapping("/successfully")
    public ResponseEntity<List<ChallengeDTO>> getSuccessfullyChallenges(@AuthenticationPrincipal UserDetails userDetail){
        try {
            List<ChallengeDTO> userChallenges = challengeService.getSuccessfullyUserChallenges(userService.getUser(userDetail.getUsername()));
            return ResponseEntity.ok(userChallenges);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Gets all (past and future) challenges of a user sorted by endDate
    @GetMapping("/")
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges(@AuthenticationPrincipal UserDetails userDetail){
        try {
            List<ChallengeDTO> userChallenges = challengeService.getAllUserChallenges(userService.getUser(userDetail.getUsername()));
            return ResponseEntity.ok(userChallenges);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
