package com.example.mux.statistic.model.dto;

import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
import com.example.mux.user.model.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GroupStepsDTO {
    Group group;
    float steps;

    public GroupStepsDTO(Group group, int steps){
        if(group.getNumberOfEmployees()==0){
            setSteps(0);
        }else{
            setSteps(steps / group.getNumberOfEmployees());
        }
        setGroup(group);
    }
}
