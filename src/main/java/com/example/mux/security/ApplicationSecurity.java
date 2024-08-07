package com.example.mux.security;

import com.example.mux.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class ApplicationSecurity {
    private final JwtTokenFilter jwtTokenFilter;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/users/self").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/users/register/**").permitAll()
                        .requestMatchers("/users/password/reset").permitAll()
                        .requestMatchers( "/users/password/reset/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/logout").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/groups/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/groups/*/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/groups").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/days").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/days/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/statistics/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/statistics/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/statistics/groups/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/statistics/groups").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/statistics/groups/*/users").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/challenges/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/competition").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}