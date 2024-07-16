package com.example.mux.day.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DayDurationDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
