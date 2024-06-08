package com.example.mux.day.service;

import com.example.mux.day.model.Day;
import com.example.mux.day.model.dto.DayStepsDTO;
import com.example.mux.day.model.dto.DurationStepsDTO;
import com.example.mux.day.repository.DayRepository;
import com.example.mux.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DayService {
    private final DayRepository dayRepository;

    @Transactional
    public void deleteDay(LocalDate date, User user){
        dayRepository.deleteByDateAndUser(date, user);//TODO check if competition started
    }

    public Day updateDay(LocalDate date, User user, DayStepsDTO daySteps) throws EntityNotFoundException{//TODO check if competition started
        Day day = dayRepository.findByDateAndUser(date, user).orElseThrow(() -> new EntityNotFoundException("Day for this date and user does not exist."));
        day.setSteps(daySteps.getSteps());
        return dayRepository.save(day);
    }

    public List<Day> getDays(User user){
        return dayRepository.findAllByUser(user);//TODO check if competition started
    }

    public List<Day> addDays(User user, DurationStepsDTO durationSteps) throws NullPointerException{//TODO check if competition started
        List<Day> days = new LinkedList<>();

        if(durationSteps.getStartDate() == null && durationSteps.getEndDate() == null){
            throw new NullPointerException("There is no given date.");
        }

        LocalDate startDate = durationSteps.getStartDate() != null ? durationSteps.getStartDate() : durationSteps.getEndDate();
        LocalDate endDate = durationSteps.getEndDate() != null ? durationSteps.getEndDate() : durationSteps.getStartDate();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        int stepNumberPerDay = (int) (durationSteps.getSteps() / (double) daysBetween);
        int biggerStepNumber = (int) (stepNumberPerDay + durationSteps.getSteps() - stepNumberPerDay * daysBetween);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int steps = stepNumberPerDay;
            if(startDate.equals(date)){
                steps = biggerStepNumber;
            }

            Optional<Day> dayOptional = dayRepository.findByDateAndUser(date, user);
            Day day = null;

            if(dayOptional.isPresent()){
                day = dayOptional.get();
            }else{
                day = new Day(date, steps, user);
            }

            days.add(day);
        }
        return dayRepository.saveAll(days);
    }
}
