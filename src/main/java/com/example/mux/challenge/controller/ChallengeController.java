package com.example.mux.challenge.controller;

import com.example.mux.challenge.model.dto.UserChallengeDTO;
import com.example.mux.challenge.service.ChallengeService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/challenges")
@AllArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserService userService;

    @GetMapping("/{date}")
    public ResponseEntity<List<UserChallengeDTO>> getChallenges(@PathVariable LocalDate date, @AuthenticationPrincipal UserDetails userDetail){
        try {
            List<UserChallengeDTO> userChallenges = challengeService.getUserChallenge(userService.getUser(userDetail.getUsername()),date);
            return ResponseEntity.ok(userChallenges);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
