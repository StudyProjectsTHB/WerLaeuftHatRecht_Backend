package com.example.mux.statistic.service;

import com.example.mux.day.model.Day;
import com.example.mux.day.service.DayService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.service.GroupService;
import com.example.mux.statistic.model.dto.GroupStepsDTO;
import com.example.mux.statistic.model.dto.StatisticDurationDTO;
import com.example.mux.statistic.model.dto.UserStepsDTO;
import com.example.mux.user.model.User;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Service
public class StatisticService {
    private final UserService userService;
    private final DayService dayService;
    private final GroupService groupService;

    public UserStepsDTO createUserStatistic(int userID, StatisticDurationDTO statisticDuration) throws EntityNotFoundException {
        User user = userService.getUser(userID);
        return createUserStatistic(user, statisticDuration);
    }

    public UserStepsDTO createUserStatistic(User user, StatisticDurationDTO statisticDuration) {
        List<Day> days = dayService.getDaysBetween(user, statisticDuration.getStartDate(), statisticDuration.getEndDate());

        return new UserStepsDTO(user, dayService.getStepSum(days));
    }

    public List<UserStepsDTO> createUserStatistics(StatisticDurationDTO statisticDuration){
        return createUserStatisticsForUsers(userService.getUsers(), statisticDuration);
    }

    public List<UserStepsDTO> createGroupUserStatistic(Group group, StatisticDurationDTO statisticDuration){
        List<User> users = userService.getUsers(group);
        return createUserStatisticsForUsers(users, statisticDuration);
    }

    private List<UserStepsDTO> createUserStatisticsForUsers(List<User> users, StatisticDurationDTO statisticDuration){
        List<UserStepsDTO> userSteps = new LinkedList<>();
        for(User user: users){
            userSteps.add(createUserStatistic(user, statisticDuration));
        }
        return userSteps;
    }

    public List<UserStepsDTO> createGroupUserStatistic(int groupID, StatisticDurationDTO statisticDurationDTO) throws EntityNotFoundException {
        Group group = groupService.getGroup(groupID);
        return createGroupUserStatistic(group, statisticDurationDTO);
    }

    public GroupStepsDTO createGroupStatistic(int groupID, StatisticDurationDTO statisticDuration) throws EntityNotFoundException {
        Group group = groupService.getGroup(groupID);
        return createGroupStatistic(group, statisticDuration);
    }

    public GroupStepsDTO createGroupStatistic(Group group, StatisticDurationDTO statisticDuration) {
        List<Day> days = dayService.getDaysBetween(group, statisticDuration.getStartDate(), statisticDuration.getEndDate());

        return new GroupStepsDTO(group, dayService.getStepSum(days));
    }

    public List<GroupStepsDTO> createGroupStatistics(StatisticDurationDTO statisticDuration){
        List<GroupStepsDTO> userSteps = new LinkedList<>();
        for(Group group: groupService.getGroups()){
            userSteps.add(createGroupStatistic(group, statisticDuration));
        }
        return userSteps;
    }
}
