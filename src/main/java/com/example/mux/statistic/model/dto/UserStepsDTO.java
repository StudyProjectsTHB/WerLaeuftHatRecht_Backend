package com.example.mux.statistic.model.dto;

import com.example.mux.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserStepsDTO implements Comparable<UserStepsDTO>{
    private String noun;
    private String adjective;
    private int id;
    int steps;

    public UserStepsDTO(User user, int steps){
        setSteps(steps);
        setNoun(user.getNoun());
        setAdjective(user.getAdjective());
        setId(user.getID());
    }

    @Override
    public int compareTo(UserStepsDTO o) {
        return Integer.compare(o.getSteps(), this.steps);
    }
}
