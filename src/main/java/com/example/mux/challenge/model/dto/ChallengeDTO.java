package com.example.mux.challenge.model.dto;

import com.example.mux.challenge.model.Challenge;
import com.example.mux.challenge.model.ChallengeTypeEnum;
import com.example.mux.day.model.Day;
import com.example.mux.user.model.User;
import com.example.mux.user.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.min;

@Getter
@Setter
@AllArgsConstructor
public class ChallengeDTO {

    private String challengeString;
    private String progressString;
    private boolean completed;

    public ChallengeDTO(Challenge challenge, User user) {
        String challengeString = Stream.of(challenge.getChallengeType().getPrefix(), Integer.toString(challenge.getTime()), challenge.getChallengeType().getTimeUnit(), Integer.toString(challenge.getAmount()), challenge.getChallengeType().getAmountUnit())
                .filter(s -> s != null && !s.isEmpty() && !s.equals("0"))
                .collect(Collectors.joining(" "));

        int goal = 0;
        int currentDone = 0;
        ArrayList<Day> relevantDays = new ArrayList<>();
        for(Day day : user.getDays()) {
            if (!(day.getDate().isBefore(challenge.getStartDate()) || day.getDate().isAfter(challenge.getEndDate()))) {
                relevantDays.add(day);
            }
        }
        relevantDays.sort(Comparator.comparing(Day::getDate));
        if (challenge.getChallengeType().getType() == ChallengeTypeEnum.CONSECUTIVELY) {
            goal = challenge.getTime();
            int temp = 0;
            int max_days = 0;
            LocalDate lastDay = null;
            for (Day day : relevantDays) {
                if (lastDay == null || !day.getDate().minusDays(1).isEqual(lastDay)) {
                    temp = 0;
                }
                lastDay = day.getDate();
                if (day.getSteps() >= challenge.getAmount()) {
                    temp++;
                    if (temp > max_days)
                        max_days = temp;
                } else {
                    temp = 0;
                }
                if (temp >= goal) {
                    break;
                }
            }
            currentDone = max_days;
        } else if (challenge.getChallengeType().getType() == ChallengeTypeEnum.EACH_DAY) {
            goal = challenge.getTime();
            int temp = 0;
            for (Day day : relevantDays) {
                if (day.getSteps() >= challenge.getAmount()) {
                    temp++;
                }
                if (temp >= goal) {
                    break;
                }
            }
            currentDone = temp;
        } else if (challenge.getChallengeType().getType() == ChallengeTypeEnum.TOTAL_DAYS) {
            goal = challenge.getAmount();
            relevantDays.sort(Comparator.comparing(Day::getSteps, Comparator.reverseOrder()));
            List<Day> chooseDays = relevantDays.subList(0, min(challenge.getTime(), relevantDays.size()));
            for (Day day : chooseDays) {
                currentDone += day.getSteps();
            }
        } else if (challenge.getChallengeType().getType() == ChallengeTypeEnum.TOTAL_STEPS) {
            goal = challenge.getAmount();
            for (Day day : relevantDays) {
                currentDone += day.getSteps();
            }
        }
        String progressString = min(currentDone, goal) + "/" + goal + " " + challenge.getChallengeType().getPrimaryUnit();

        this.setChallengeString(challengeString);
        this.setProgressString(progressString);
        this.setCompleted(currentDone >= goal);

    }

}
