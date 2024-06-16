package com.example.mux.user.model.dto;

import com.example.mux.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private String accessToken;
    private UserDTO user;

    public AuthenticationResponseDTO(String accessToken, User user){
        setAccessToken(accessToken);
        setUser(new UserDTO(user));
    }

}
