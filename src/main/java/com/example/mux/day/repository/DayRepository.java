package com.example.mux.day.repository;

import com.example.mux.day.model.Day;
import com.example.mux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayRepository extends JpaRepository<Day, Integer> {
    void deleteByDateAndUser(LocalDate date, User user);
    Optional<Day> findByDateAndUser(LocalDate date, User user);
    List<Day> findAllByUser(User user);
}
