package com.example.mux.day.model.dto;

import com.example.mux.day.model.Day;
import com.example.mux.util.StepsToKilometersWithKilometers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DayDTO extends StepsToKilometersWithKilometers {
    private LocalDate date;
    private int steps;

    public DayDTO(Day day){
        setDate(day.getDate());
        setSteps(day.getSteps());
        this.calculateAndSetKilometers(this.steps, day.getUser().getHeight(), day.getUser().getStepSize());
    }

    public static List<DayDTO> fromDayList(List<Day> days){
        List<DayDTO> dayDTOS = new LinkedList<>();
        days.forEach(day -> dayDTOS.add(new DayDTO(day)));
        return dayDTOS;
    }
}
