package com.example.mux.statistic.model.dto;

import com.example.mux.user.model.User;
import com.example.mux.util.StepsToKilometersWithKilometers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SingleUserStepsDTO extends StepsToKilometersWithKilometers {
    private String noun;
    private String adjective;
    private int steps;
    private double stepGoalKilometers;


    public SingleUserStepsDTO(User user, int steps){
        setSteps(steps);
        setNoun(user.getNoun());
        setAdjective(user.getAdjective());
        calculateAndSetKilometers(steps, user.getHeight(), user.getStepSize());
        setStepGoalKilometers(calculateKilometers(user.getStepGoal(), user.getHeight(), user.getStepSize()));
    }

}
