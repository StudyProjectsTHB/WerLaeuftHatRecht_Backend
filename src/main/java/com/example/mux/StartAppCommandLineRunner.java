package com.example.mux;

import com.example.mux.competition.model.dto.UpdateCompetitionDTO;
import com.example.mux.competition.service.CompetitionService;
import com.example.mux.day.model.Day;
import com.example.mux.day.repository.DayRepository;
import com.example.mux.day.service.DayService;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.service.GroupService;
import com.example.mux.user.model.User;
import com.example.mux.user.service.AvailableNameService;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@AllArgsConstructor
public class StartAppCommandLineRunner implements CommandLineRunner {
    private final InitProperties initProperties;
    private final GroupService groupService;
    private final UserService userService;
    private final DayService dayService;
    private final CompetitionService competitionService;

    private final ArrayList<String> userAdjectives = new ArrayList<>(Arrays.asList("schneller", "schöner", "perfekter", "attraktiver", "fleissiger", "eifriger", "herzlicher", "glücklicher", "lieber", "lustiger", "selbstbewusster", "sympathischer", "starker", "stolzer", "toller", "überragender", "vorbildlicher", "wendiger", "zauberhafter"));

    private final ArrayList<String> userNouns = new ArrayList<>(Arrays.asList("Löwe", "Pinguin", "Elch", "Bär", "Affe", "Fuchs", "Luchs", "Igel", "Tiger", "Wolf"));
    private final DayRepository dayRepository;

    @Override
    public void run(String... args) throws Exception {
        competitionService.createInitialCompetition();
        competitionService.updateCompetition(new UpdateCompetitionDTO(LocalDate.now().minusDays(32), LocalDate.now().plusDays(35), false));
        generateUserNames();
        System.out.println("\n\nCompetition Names left: " + AvailableNameService.getNumberOfAvailableNames());

        if (checkIfDaysUsersGroupsEmpty()) {
            Random random = new Random();

            // Create groups
            List<GroupCreationDTO> groupDTOs = new ArrayList<>();
            for (String groupName : initProperties.getGroupNames()) {
                groupDTOs.add(new GroupCreationDTO(groupName, random.nextInt(40) + 10));
            }
            List<Group> groups = groupService.createGroups(groupDTOs);

            // Create Users
            ArrayList<User> users = new ArrayList<>();
            int numUsers = groups.size() * 10;
            for (int i = 0; i < numUsers; i++) {
                users.add(new User("user" + i + "@gericht-brb.xx", (i == 0) || (random.nextFloat() < 0.1)));
            }
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                u.setGroup((i < 5) ? groups.get(0) : ((i >= users.size() - 2) ? groups.get(groups.size() - 1) : groups.get(random.nextInt(groups.size() - 1))));
                u.setPassword("12345678");
                u.setCompetitionName(AvailableNameService.getAvailableName());
                int stepFactor = random.nextInt(130) + 20;
                if (random.nextFloat() < 0.5) {
                    int stepGoal = (random.nextInt(100) + 100) * stepFactor;
                    u.setStepGoal(stepGoal == 0 ? 10000 : stepGoal);
                }

                System.out.println("\n\nCreated User: " + u);

                boolean stepsEveryDay = random.nextFloat() < 0.5;
                ArrayList<Day> days = new ArrayList<>();
                for (LocalDate date = competitionService.getCompetition().getStartDate(); !date.isAfter(LocalDate.now()); date = date.plusDays(1)) {
                    if (stepsEveryDay || random.nextFloat() < 0.8) {
                        days.add(new Day(date, (random.nextInt(stepFactor) * random.nextInt(100) + 100 * stepFactor), u));
                        System.out.print(days.get(days.size() - 1).getSteps() + " ");
                    } else {
                        System.out.print("0 ");
                    }
                }
                userService.createAndRegisterIfNotExist(u);
                dayRepository.saveAll(days);
            }

        }
        System.out.println("\n\nCompetition Names left: " + AvailableNameService.getNumberOfAvailableNames());

    }

    private boolean checkIfDaysUsersGroupsEmpty() {
        return dayService.getDays().isEmpty() && userService.getUsers().isEmpty() && groupService.getGroups().isEmpty();
    }

    private void createTestData() {

        Group group1 = groupService.createIfNotExist(new Group("OLG Brandenburg", 10));
        Group group2 = groupService.createIfNotExist(new Group("AG Potsdam", 12));
        User user1 = new User("admin@admin.de", true);
        user1.setPassword("admin");
        user1.setGroup(group1);
        User user2 = new User("user@user.de", false);
        user2.setPassword("user");
        user2.setGroup(group1);

        User user3 = new User("user2@user2.de", false);
        user3.setPassword("user2");
        user3.setGroup(group2);

        userService.createAndRegisterIfNotExist(user1);
        userService.createAndRegisterIfNotExist(user2);
        userService.createAndRegisterIfNotExist(user3);

    }

    @SneakyThrows
    private void generateUserNames() {
        AvailableNameService.generateAvailableNames(userAdjectives, userNouns);
    }
}
