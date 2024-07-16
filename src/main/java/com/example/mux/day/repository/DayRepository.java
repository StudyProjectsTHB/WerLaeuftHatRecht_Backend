package com.example.mux.day.repository;

import com.example.mux.day.model.Day;
import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Integer> {
    void deleteByDateAndUser(LocalDate date, User user);
    void deleteByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    Optional<Day> findByDateAndUser(LocalDate date, User user);
    List<Day> findAllByUser(User user);
    List<Day> findAllByUser_Group(Group group);
    List<Day> findAllByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Day> findAllByUserAndDateGreaterThanEqual(User user, LocalDate startDate);
    List<Day> findAllByUserAndDateLessThanEqual(User user, LocalDate endDate);
    List<Day> findAllByUser_GroupAndDateBetween(Group group, LocalDate startDate, LocalDate endDate);
    List<Day> findAllByUser_GroupAndDateGreaterThanEqual(Group group, LocalDate startDate);
    List<Day> findAllByUser_GroupAndDateLessThanEqual(Group group, LocalDate endDate);
    boolean existsByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
