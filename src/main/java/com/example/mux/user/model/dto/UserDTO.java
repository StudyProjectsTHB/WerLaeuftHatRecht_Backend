package com.example.mux.user.model.dto;

import com.example.mux.group.model.dto.GroupDTO;
import com.example.mux.user.model.User;
import com.example.mux.util.StepsToKilometers;
import com.example.mux.util.StepsToKilometersWithKilometers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO extends StepsToKilometers {
    private String email;
    private boolean isAdmin;
    private String noun;
    private String adjective;
    private GroupDTO group;
    private int stepGoal;
    private double stepGoalKilometers;
    private int ID;
    private Integer height;

    private Integer stepSize;

    public UserDTO(User user){
        setEmail(user.getEmail());
        setNoun(user.getNoun());
        setAdjective(user.getAdjective());
        setGroup(new GroupDTO(user.getGroup()));
        this.isAdmin = user.isAdmin();
        setID(user.getID());
        setStepGoal(user.getStepGoal());
        setHeight(user.getHeight());
        setStepSize(user.getStepSize());
        setStepGoalKilometers(calculateKilometers(user.getStepGoal(), user.getHeight(), user.getStepSize()));

    }

    public static List<UserDTO> fromUserList(List<User> users){
        List<UserDTO> userDTOS = new LinkedList<>();
        users.forEach(user -> userDTOS.add(new UserDTO(user)));
        return userDTOS;
    }
}
