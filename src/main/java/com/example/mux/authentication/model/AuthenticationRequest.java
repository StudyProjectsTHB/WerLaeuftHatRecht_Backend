package com.example.mux.authentication.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationRequest {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
