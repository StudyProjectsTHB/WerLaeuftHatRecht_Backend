package com.example.mux.statistic.model.dto;

import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupDTO;
import com.example.mux.user.model.User;
import com.example.mux.user.model.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GroupStepsDTO implements Comparable<GroupStepsDTO>{
    private GroupDTO group;
    private int steps;
    private float stepsPerUser;

    public GroupStepsDTO(Group group, int steps) {
        setSteps(steps);
        setGroup(new GroupDTO(group));
        if(group.getNumberOfEmployees() > 0){
            setStepsPerUser(steps / group.getNumberOfEmployees());
        }else{
            setStepsPerUser(0);
        }
    }

    @Override
    public int compareTo(GroupStepsDTO o) {
        return Float.compare(o.getStepsPerUser(), this.stepsPerUser);
    }
}
