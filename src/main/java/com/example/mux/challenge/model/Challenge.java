package com.example.mux.challenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_type_ID")
    private ChallengeType challengeType;

    public Challenge(int time, int amount, LocalDate startDate, LocalDate endDate, ChallengeType challengeType) {
        this.time = time;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.challengeType = challengeType;
    }

}
