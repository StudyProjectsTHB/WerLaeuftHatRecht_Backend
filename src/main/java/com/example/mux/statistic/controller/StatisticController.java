package com.example.mux.statistic.controller;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.statistic.model.dto.GroupStepsDTO;
import com.example.mux.statistic.model.dto.SingleUserStepsDTO;
import com.example.mux.statistic.model.dto.StatisticDurationDTO;
import com.example.mux.statistic.model.dto.UserStepsDTO;
import com.example.mux.statistic.service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/users/{ID}")
    @ResponseBody
    public ResponseEntity<SingleUserStepsDTO> createUserStatistic(@PathVariable int ID, @RequestBody StatisticDurationDTO statisticDuration){
        try {
            return ResponseEntity.ok(statisticService.createSingleUserStatistic(ID, statisticDuration));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public List<UserStepsDTO> createUserStatistics(@RequestBody StatisticDurationDTO statisticDuration){
        return statisticService.createUserStatistics(statisticDuration);
    }

    @GetMapping("/groups/{ID}")
    @ResponseBody
    public ResponseEntity<GroupStepsDTO> createGroupStatistic(@PathVariable int ID, @RequestBody StatisticDurationDTO statisticDuration){
        try {
            return ResponseEntity.ok(statisticService.createGroupStatistic(ID, statisticDuration));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/groups/{ID}/users")
    @ResponseBody
    public ResponseEntity<List<UserStepsDTO>> createGroupUserStatistic(@PathVariable int ID, @RequestBody StatisticDurationDTO statisticDuration){
        try {
            return ResponseEntity.ok(statisticService.createGroupUserStatistic(ID, statisticDuration));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<GroupStepsDTO> createGroupStatistics(@RequestBody StatisticDurationDTO statisticDuration){
        return statisticService.createGroupStatistics(statisticDuration);
    }
}
