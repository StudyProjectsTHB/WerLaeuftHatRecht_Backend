package com.example.mux;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.init")
@Getter
@Setter
public class InitProperties {
    private String email;
    private String groupName;
}