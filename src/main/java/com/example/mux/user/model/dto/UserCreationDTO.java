package com.example.mux.user.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreationDTO {
    String email;
    boolean isAdmin;
    int groupId;

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
}
