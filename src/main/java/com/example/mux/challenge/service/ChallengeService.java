package com.example.mux.challenge.service;

import com.example.mux.challenge.model.dto.UserChallengeDTO;
import com.example.mux.challenge.repository.ChallengeRepository;
import com.example.mux.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    public List<UserChallengeDTO> getUserChallenge(User user, LocalDate date){
        HashSet<User> users = new HashSet<>();
        users.add(user);
        List<com.example.mux.challenge.model.Challenge> allUserChallenges = challengeRepository.findAllByUsers(users);
        ArrayList<UserChallengeDTO> userChallengeDTOs = new ArrayList<>();

        for(com.example.mux.challenge.model.Challenge challenge : allUserChallenges) {
            if (!(date.isBefore(challenge.getStartDate()) || date.isAfter(challenge.getEndDate()))) {
                userChallengeDTOs.add(new UserChallengeDTO(challenge, user));
            }
        }

        return userChallengeDTOs;
    }
}
