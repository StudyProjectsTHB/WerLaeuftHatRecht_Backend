package com.example.mux.day.controller;

import com.example.mux.day.model.dto.DayDTO;
import com.example.mux.day.model.dto.StepsDTO;
import com.example.mux.day.model.dto.DurationStepsDTO;
import com.example.mux.day.service.DayService;
import com.example.mux.exception.CompetitionNotStartedException;
import com.example.mux.exception.DaysNotInCompetitionException;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/days")
@AllArgsConstructor
public class DayController {

    private final UserService userService;
    private final DayService dayService;

    @DeleteMapping("/{date}")
    public ResponseEntity<?> deleteDay(@PathVariable LocalDate date, @AuthenticationPrincipal UserDetails userDetail) {
        try {
            dayService.deleteDay(date, userService.getUser(userDetail.getUsername()));
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (CompetitionNotStartedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteDays(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate, @AuthenticationPrincipal UserDetails userDetail){
        try {
            dayService.deleteDays(startDate, endDate, userService.getUser(userDetail.getUsername()));
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{date}")
    @ResponseBody
    public ResponseEntity<DayDTO> updateDay(@PathVariable LocalDate date, @RequestBody StepsDTO daySteps, @AuthenticationPrincipal UserDetails userDetail) {
        try {
            DayDTO day = new DayDTO(dayService.updateDay(date, userService.getUser(userDetail.getUsername()), daySteps));
            return ResponseEntity.ok(day);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch (CompetitionNotStartedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<List<DayDTO>> addDays(@RequestBody DurationStepsDTO durationSteps, @AuthenticationPrincipal UserDetails userDetail) {
        try {
            List<DayDTO> days = DayDTO.fromDayList(dayService.addDays(userService.getUser(userDetail.getUsername()), durationSteps));
            return ResponseEntity.ok(days);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (CompetitionNotStartedException | DaysNotInCompetitionException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DayDTO>> getDays(@AuthenticationPrincipal UserDetails userDetail){
        try {
            List<DayDTO> days = DayDTO.fromDayList(dayService.getDays(userService.getUser(userDetail.getUsername())));
            return ResponseEntity.ok(days);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
