package com.example.mux.user.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequestDTO {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
