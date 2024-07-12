package com.example.mux.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserDTO {
    private Integer stepGoal;
    private Integer height;
    private Integer stepSize;
    private Integer groupId;
    private String email;
    private Boolean isAdmin;
}
