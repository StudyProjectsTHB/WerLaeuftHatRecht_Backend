package com.example.mux.statistic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatisticDurationDTO {
    LocalDate startDate;
    LocalDate endDate;
}
