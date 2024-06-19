package com.example.mux.day.service;

import com.example.mux.competition.model.Competition;
import com.example.mux.competition.service.CompetitionService;
import com.example.mux.day.model.Day;
import com.example.mux.day.model.dto.StepsDTO;
import com.example.mux.day.model.dto.DurationStepsDTO;
import com.example.mux.day.repository.DayRepository;
import com.example.mux.exception.CompetitionNotStartedException;
import com.example.mux.exception.DaysNotInCompetitionException;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
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
    private final CompetitionService competitionService;

    @Transactional
    public void deleteDay(LocalDate date, User user) throws CompetitionNotStartedException {
        if (competitionService.competitionExists()) {
            dayRepository.deleteByDateAndUser(date, user);
        } else {
            throw new CompetitionNotStartedException();
        }
    }

    public Day updateDay(LocalDate date, User user, StepsDTO daySteps) throws EntityNotFoundException, CompetitionNotStartedException {
        if (competitionService.competitionExists()) {
            Day day = dayRepository.findByDateAndUser(date, user).orElseThrow(() -> new EntityNotFoundException("Day for this date and user does not exist."));
            day.setSteps(daySteps.getSteps());
            return dayRepository.save(day);
        } else {
            throw new CompetitionNotStartedException();
        }
    }

    public List<Day> getDays(User user) {
        return dayRepository.findAllByUser(user);
    }

    public List<Day> getDays() {
        return dayRepository.findAll();
    }

    public List<Day> addDays(User user, DurationStepsDTO durationSteps) throws NullPointerException, CompetitionNotStartedException, EntityNotFoundException, DaysNotInCompetitionException {
        if (!competitionService.competitionExists()) {
            throw new CompetitionNotStartedException();
        }
        List<Day> days = new LinkedList<>();

        if (durationSteps.getStartDate() == null && durationSteps.getEndDate() == null) {
            throw new NullPointerException("There is no given date.");
        }

        LocalDate startDate = durationSteps.getStartDate() != null ? durationSteps.getStartDate() : durationSteps.getEndDate();
        LocalDate endDate = durationSteps.getEndDate() != null ? durationSteps.getEndDate() : durationSteps.getStartDate();
        if (endDate.isAfter(competitionService.getCompetition().getEndDate())) {
            endDate = competitionService.getCompetition().getEndDate();
        }
        if (startDate.isBefore(competitionService.getCompetition().getStartDate())) {
            startDate = competitionService.getCompetition().getStartDate();
        }
        if (startDate.isAfter(endDate)) {
            throw new DaysNotInCompetitionException();
        }
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        int stepNumberPerDay = (int) (durationSteps.getSteps() / (double) daysBetween);
        int biggerStepNumber = (int) (stepNumberPerDay + durationSteps.getSteps() - stepNumberPerDay * daysBetween);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int steps = stepNumberPerDay;
            if (startDate.equals(date)) {
                steps = biggerStepNumber;
            }

            Optional<Day> dayOptional = dayRepository.findByDateAndUser(date, user);
            Day day = null;

            if (dayOptional.isPresent()) {
                day = dayOptional.get();
                day.setSteps(day.getSteps() + steps);
            } else {
                day = new Day(date, steps, user);
            }

            days.add(day);
        }
        return dayRepository.saveAll(days);
    }

    public List<Day> getDaysBetween(User user, LocalDate startDate, LocalDate endDate) {
        List<Day> resultDays = null;
        if (startDate == null && endDate == null) {
            resultDays = dayRepository.findAllByUser(user);
        } else if (startDate != null) {
            resultDays = dayRepository.findAllByUserAndDateGreaterThanEqual(user, startDate);
        } else if (endDate != null) {
            resultDays = dayRepository.findAllByUserAndDateLessThanEqual(user, endDate);
        } else {
            resultDays = dayRepository.findAllByUserAndDateBetween(user, startDate, endDate);
        }
        return resultDays;
    }

    public List<Day> getDaysBetween(Group group, LocalDate startDate, LocalDate endDate) {
        List<Day> resultDays = null;
        if (startDate == null && endDate == null) {
            resultDays = dayRepository.findAllByUser_Group(group);
        } else if (startDate != null) {
            resultDays = dayRepository.findAllByUser_GroupAndDateGreaterThanEqual(group, startDate);
        } else if (endDate != null) {
            resultDays = dayRepository.findAllByUser_GroupAndDateLessThanEqual(group, endDate);
        } else {
            resultDays = dayRepository.findAllByUser_GroupAndDateBetween(group, startDate, endDate);
        }
        return resultDays;
    }

    public int getStepSum(List<Day> days) {
        if (days == null) {
            return 0;
        } else {
            int result = 0;
            for (Day day : days) {
                result += day.getSteps();
            }
            return result;
        }
    }

    public void deleteDays() {
        dayRepository.deleteAll();
    }
}
