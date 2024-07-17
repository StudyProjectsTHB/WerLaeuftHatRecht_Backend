package com.example.mux.group.model.dto;

import com.example.mux.group.model.Group;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class GroupDTO {
    private Integer ID;

    @NotNull
    private String name;

    private int numberOfEmployees;

    public GroupDTO(Group group){
        this.ID = group.getID();
        this.name = group.getName();
        this.numberOfEmployees = group.getNumberOfEmployees();
    }

    public static List<GroupDTO> fromGroupList(List<Group> groups){
        List<GroupDTO> groupDTOs = new LinkedList<>();
        groups.forEach(group -> groupDTOs.add(new GroupDTO(group)));
        return groupDTOs;
    }
}
