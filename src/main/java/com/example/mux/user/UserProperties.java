package com.example.mux.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.user")
@Getter
@Setter
public class UserProperties {
    private int expiresAfter;
    private String secretKey;
    private int remindingDayLimit;
    private int initStepGoal;
    private String baseUrl;
    private String registerUserPath;
    private String resetPasswordPath;

    public String getFullRegistrationUrl(String token) {
        return baseUrl + registerUserPath + token;
    }

    public String getFullResetPasswordUrl(String token) {
        return baseUrl + resetPasswordPath + token;
    }
}