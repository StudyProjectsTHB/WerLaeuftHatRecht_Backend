package com.example.mux.statistic.model.dto;

import com.example.mux.user.model.User;
import com.example.mux.util.StepsToKilometers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SingleUserStepsDTO extends StepsToKilometers {
    private String noun;
    private String adjective;
    int steps;

    public SingleUserStepsDTO(User user, int steps){
        setSteps(steps);
        setNoun(user.getNoun());
        setAdjective(user.getAdjective());
        calculateKilometers(steps, user.getHeight(), user.getStepSize());
    }

}
