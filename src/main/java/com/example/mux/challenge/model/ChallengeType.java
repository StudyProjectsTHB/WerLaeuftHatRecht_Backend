package com.example.mux.challenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "\"challenge_type\"")
public class ChallengeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private String prefix;

    private String timeUnit;

    private String amountUnit;

    private String primaryUnit;

    private ChallengeTypeEnum type;

    public ChallengeType(String prefix, String timeUnit, String amountUnit, String primaryUnit, ChallengeTypeEnum type){
        this.prefix = prefix;
        this.timeUnit = timeUnit;
        this.amountUnit = amountUnit;
        this.primaryUnit = primaryUnit;
        this.type = type;
    }
}
