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
    private Group group;
    private int steps;

    public GroupStepsDTO(Group group, int steps) {
        setSteps(steps);
        setGroup(group);
    }
}
