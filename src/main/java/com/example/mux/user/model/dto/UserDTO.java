package com.example.mux.user.model.dto;

import com.example.mux.day.model.Day;
import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String email;
    private boolean isAdmin;
    private String noun;
    private String adjective;
    private Group group;
    private int ID;

    public UserDTO(User user){
        setEmail(user.getEmail());
        setNoun(user.getNoun());
        setAdjective(user.getAdjective());
        setGroup(user.getGroup());
        this.isAdmin = user.isAdmin();
        setID(user.getID());
    }

    public static List<UserDTO> fromUserList(List<User> users){
        List<UserDTO> userDTOS = new LinkedList<>();
        users.forEach(user -> userDTOS.add(new UserDTO(user)));
        return userDTOS;
    }
}
