package com.example.mux.challenge.service;

import com.example.mux.challenge.model.Challenge;
import com.example.mux.challenge.model.ChallengeType;
import com.example.mux.challenge.model.ChallengeTypeEnum;
import com.example.mux.challenge.model.dto.UserChallengeDTO;
import com.example.mux.challenge.repository.ChallengeRepository;
import com.example.mux.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    static Random random = new Random();

    public List<UserChallengeDTO> getUserChallenge(User user, LocalDate date){
        HashSet<Challenge> challenges = new HashSet<>();
        challenges.addAll(challengeRepository.findAllByStartDateLessThanEqual(date));
        challenges.addAll(challengeRepository.findAllByEndDateGreaterThan(date));

        ArrayList<UserChallengeDTO> userChallengeDTOs = new ArrayList<>();

        for(Challenge challenge : challenges)
            userChallengeDTOs.add(new UserChallengeDTO(challenge, user));

        return userChallengeDTOs;
    }

    public void createChallenges(LocalDate startDate, LocalDate endDate) {createChallenges(startDate, endDate, 2);}

    public void createChallenges(LocalDate startDate, LocalDate endDate, int challengesPerWeek) {
        LocalDate firstMonday = startDate.plusDays((8-startDate.getDayOfWeek().getValue())%7);
        LocalDate lastSunday = endDate.minusDays(endDate.getDayOfWeek().getValue()%7);

        HashSet<Challenge> challenges = new HashSet<>();
        if (firstMonday.isBefore(lastSunday)) {
            LocalDate currStart = firstMonday;
            LocalDate currEnd = firstMonday.plusDays(6);
            while(!currEnd.isAfter(lastSunday)) {
                for(int i = 0; i<challengesPerWeek; i++){
                    challenges.add(createChallenge(currStart, currEnd));
                }
                currStart = currStart.plusDays(7);
                currEnd = currEnd.plusDays(7);
            }
        }
        challengeRepository.saveAll(challenges);
    }

    private Challenge createChallenge(LocalDate startDate, LocalDate endDate) {
        ChallengeTypeEnum type = ChallengeTypeEnum.randomType();
        int time = 0;
        int amount = 0;
        String prefix = "";
        String timeUnit = "";
        String amountUnit = "";
        String primaryUnit = "";

        float factor = random.nextFloat() + 0.501f;         // if low --> less day and more steps, if high --> more days and fewer steps
        if(type == ChallengeTypeEnum.CONSECUTIVELY) {
            time = (int) (4*factor);                                                                   // 2-6 days in a row
            amount = random.nextInt(4) * 500 + 4000 + Math.round(4000 / (factor*500))*500;      // 6500-14000 steps
            prefix = "Laufe an";
            timeUnit = "aufeinanderfolgenden Tagen mindestens je";
            amountUnit = "Schritte";
            primaryUnit = "Tage";
        } else if (type == ChallengeTypeEnum.EACH_DAY) {
            time = (int) (5*factor);                                                                   // 2-7 days each
            amount = random.nextInt(4) * 500 + 4000 + Math.round(4000 / (factor*500))*500;      // 6500-14000 steps
            prefix = "Laufe an";
            timeUnit = "Tagen jeweils mindestens";
            amountUnit = "Schritte";
            primaryUnit = "Tage";
        } else if (type == ChallengeTypeEnum.TOTAL_DAYS) {
            time = (int) (4*factor);                                                                        // 2-6 days together
            amount = (random.nextInt(4) * 500 + 4000 + Math.round(4000 / (factor*500))*500) * time;  // ~28000 - 39000
            prefix = "Laufe an";
            timeUnit = "Tagen mindestens jeweils";
            amountUnit = "Schritte";
            primaryUnit = "Schritte";
        } else if (type == ChallengeTypeEnum.TOTAL_STEPS) {
            amount = (int) Math.round((10000 * ((1.5 - factor) * 0.5 + 0.75))/500) * 500 * 7;               // 52500 - 87500
            prefix = "Laufe in dieser Woche insgesamt";
            timeUnit = "";
            amountUnit = "Schritte";
            primaryUnit = "Schritte";
        }
        ChallengeType tempChallengeType = new ChallengeType(prefix, timeUnit, amountUnit, primaryUnit, type);
        return new Challenge(time, amount, startDate, endDate, tempChallengeType);

    }
}
