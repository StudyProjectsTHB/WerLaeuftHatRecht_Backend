package com.example.mux.challenge.model;

import com.example.mux.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "\"challenge\"")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private int time;

    private int amount;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToMany
    Set<User> users;

    @ManyToOne
    @JoinColumn(name = "challenge_type_ID")
    private ChallengeType challengeType;

}
