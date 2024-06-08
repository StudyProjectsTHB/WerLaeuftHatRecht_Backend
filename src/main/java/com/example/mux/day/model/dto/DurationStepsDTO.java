package com.example.mux.day.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class DurationStepsDTO {
    LocalDate startDate;
    LocalDate endDate;
    int steps;
}
