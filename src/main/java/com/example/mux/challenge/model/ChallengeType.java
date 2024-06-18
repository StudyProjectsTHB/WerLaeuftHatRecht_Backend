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

    private String timeUnity;

    private String amountUnity;

    private String primaryUnity;

    private ChallengeTypeEnum type;
}
