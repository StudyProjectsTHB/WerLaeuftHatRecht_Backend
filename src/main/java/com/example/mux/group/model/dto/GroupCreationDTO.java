package com.example.mux.group.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class GroupCreationDTO {
    private String name;
    private int numberOfEmployees;
}
