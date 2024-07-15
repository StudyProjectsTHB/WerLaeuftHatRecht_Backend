package com.example.mux.challenge.service;

import com.example.mux.challenge.model.Challenge;
import com.example.mux.challenge.model.ChallengeType;
import com.example.mux.challenge.model.ChallengeTypeEnum;
import com.example.mux.challenge.model.dto.ChallengeDTO;
import com.example.mux.challenge.repository.ChallengeRepository;
import com.example.mux.user.model.User;
import com.example.mux.user.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final EmailService emailService;

    static Random random = new Random();

    public List<ChallengeDTO> getUserChallenges(User user, LocalDate date){

        HashSet<Challenge> challenges = new HashSet<>(challengeRepository.findAllByStartDateLessThanEqual(date));
        challenges.retainAll(challengeRepository.findAllByEndDateGreaterThanEqual(date));

        ArrayList<ChallengeDTO> challengeDTOS = new ArrayList<>();

        for(Challenge challenge : challenges)
            challengeDTOS.add(new ChallengeDTO(challenge, user));

        return challengeDTOS;
    }

    public List<ChallengeDTO> getAllUserChallenges(User user){
        HashSet<Challenge> challenges = new HashSet<>(challengeRepository.findAll());
        ArrayList<Challenge> challengesList = new ArrayList<>(challenges);
        challengesList.sort(Comparator.comparing(Challenge::getEndDate).reversed());

        ArrayList<ChallengeDTO> challengeDTOS = new ArrayList<>();

        for(Challenge challenge : challengesList)
            challengeDTOS.add(new ChallengeDTO(challenge, user));


        return challengeDTOS;
    }

    public List<ChallengeDTO> getSuccessfullyUserChallenges(User user){
        List<ChallengeDTO> challengeDTOs = getAllUserChallenges(user);
        challengeDTOs.removeIf(cDTO -> !cDTO.isCompleted());
        return challengeDTOs;
    }

    public void createChallenges(LocalDate startDate, LocalDate endDate) {createChallenges(startDate, endDate, 2);}

    public void createChallenges(LocalDate startDate, LocalDate endDate, int challengesPerWeek) {
        LocalDate firstMonday = startDate.plusDays((8-startDate.getDayOfWeek().getValue())%7);
        LocalDate lastSunday = endDate.minusDays(endDate.getDayOfWeek().getValue()%7);

        ArrayList<Challenge> challenges = new ArrayList<>();
        if (firstMonday.isBefore(lastSunday)) {
            LocalDate currStart = firstMonday;
            LocalDate currEnd = firstMonday.plusDays(6);
            while(!currEnd.isAfter(lastSunday)) {
                HashSet<ChallengeTypeEnum> lastTypes = new HashSet<>();
                for(int i = 0; i<challengesPerWeek; i++){
                    Challenge challenge = createChallenge(currStart, currEnd, lastTypes);
                    lastTypes.add(challenge.getChallengeType().getType());
                    if (lastTypes.size() >= ChallengeTypeEnum.values().length)
                        lastTypes = new HashSet<>();
                    challenges.add(challenge);
                }
                currStart = currStart.plusDays(7);
                currEnd = currEnd.plusDays(7);
            }
        }
        challengeRepository.saveAll(challenges);
    }

    private Challenge createChallenge(LocalDate startDate, LocalDate endDate, Set<ChallengeTypeEnum> excludeTypes) {
        ChallengeTypeEnum type;
        do {
            type = ChallengeTypeEnum.randomType();
        } while (excludeTypes.contains(type));
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
            timeUnit = "fortlaufenden Tagen mindestens je";
            amountUnit = "Schritte";
            primaryUnit = "Tage";
        } else if (type == ChallengeTypeEnum.EACH_DAY) {
            time = (int) (5*factor);                                                                   // 2-7 days each
            amount = random.nextInt(4) * 500 + 4000 + Math.round(4000 / (factor*500))*500;      // 6500-14000 steps
            prefix = "Laufe an";
            timeUnit = "Tagen mindestens je";
            amountUnit = "Schritte";
            primaryUnit = "Tage";
        } else if (type == ChallengeTypeEnum.TOTAL_DAYS) {
            time = (int) (4*factor);                                                                        // 2-6 days together
            amount = (random.nextInt(4) * 500 + 4000 + Math.round(4000 / (factor*500))*500) * time;  // ~28000 - 39000
            prefix = "Laufe an";
            timeUnit = "Tagen insgesamt mindestens";
            amountUnit = "Schritte";
            primaryUnit = "Schritte";
        } else if (type == ChallengeTypeEnum.TOTAL_STEPS) {
            amount = Math.round((random.nextInt(2000)+8000)/500f)*500*7;               // 52500 - 87500
            prefix = "Laufe in dieser Woche insgesamt";
            timeUnit = "";
            amountUnit = "Schritte";
            primaryUnit = "Schritte";
        }
        ChallengeType tempChallengeType = new ChallengeType(prefix, timeUnit, amountUnit, primaryUnit, type);
        return new Challenge(time, amount, startDate, endDate, tempChallengeType);

    }

    public void sendChallengeEmail(List<User> users){
        LocalDate sunday = LocalDate.now().minusDays(1);
        for(User user: users){
            List<ChallengeDTO> challengeDTOS = getUserChallenges(user, sunday);
            if(user.isEnabled() && !challengeDTOS.isEmpty()) {
                emailService.sendChallengeEmail(user, challengeDTOS);
            }
        }
    }
}
