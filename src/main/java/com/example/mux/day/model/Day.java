package com.example.mux.day.model;

import com.example.mux.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "\"day\"")
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @NotNull
    private LocalDate date;

    private int steps;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    private User user;

    public Day(LocalDate date, int steps, User user){
        setDate(date);
        setSteps(steps);
        setUser(user);
    }
}
