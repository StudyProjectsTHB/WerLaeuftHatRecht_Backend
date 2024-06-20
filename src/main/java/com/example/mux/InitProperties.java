package com.example.mux;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.init")
@Getter
@Setter
public class InitProperties {
    private List<String> email;
    private List<String> groupNames;
}