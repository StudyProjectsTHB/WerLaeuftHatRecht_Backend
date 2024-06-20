package com.example.mux.scheduler;

import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Scheduler {
    private final UserService userService;
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkActivitiesAndSendReminder(){
        userService.checkActivitiesAndSendReminder();
    }
}
