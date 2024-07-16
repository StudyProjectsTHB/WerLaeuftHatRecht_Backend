package com.example.mux;

import com.example.mux.competition.model.dto.UpdateCompetitionDTO;
import com.example.mux.competition.service.CompetitionService;
import com.example.mux.day.model.Day;
import com.example.mux.day.repository.DayRepository;
import com.example.mux.day.service.DayService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.service.GroupService;
import com.example.mux.user.model.User;
import com.example.mux.user.repository.UserRepository;
import com.example.mux.user.service.AvailableNameService;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
    public final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        competitionService.createInitialCompetition();
        generateUserNames();
        System.out.println("\n\nCompetition Names left: " + AvailableNameService.getNumberOfAvailableNames());

        if (checkIfDaysUsersGroupsEmpty()) {

            if (initProperties.getIsScreencast())
                createLiveData();
            else
                createTestData();

        }
        System.out.println("\n\nCompetition Names left: " + AvailableNameService.getNumberOfAvailableNames());

    }

    private boolean checkIfDaysUsersGroupsEmpty() {
        return dayService.getDays().isEmpty() && userService.getUsers().isEmpty() && groupService.getGroups().isEmpty();
    }

    private void createTestData() throws EntityNotFoundException {
        competitionService.updateCompetition(new UpdateCompetitionDTO(LocalDate.now().minusDays(32), LocalDate.now().plusDays(35), false));
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
            users.add(new User("user" + i + "@gericht-brb.dummy", (i == 0) || (random.nextFloat() < 0.1)));
        }
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            u.setGroup((i < 5) ? groups.get(0) : ((i >= users.size() - 2) ? groups.get(groups.size() - 1) : groups.get(random.nextInt(groups.size() - 1))));
            u.setPassword("12345678");
            u.setCompetitionName(AvailableNameService.getAvailableName());
            int stepFactor = (i==0) ? 150 : random.nextInt(130) + 20;
            if (random.nextFloat() < 0.5) {
                int stepGoal = (random.nextInt(100) + 100) * stepFactor;
                u.setStepGoal(stepGoal == 0 ? 10000 : stepGoal);
            }

            System.out.println("\n\nCreated User: " + u);

            boolean stepsEveryDay = i == 0 || random.nextFloat() < 0.5;
            HashSet<Day> days = new HashSet<>();
            for (LocalDate date = competitionService.getCompetition().getStartDate(); date.isBefore(LocalDate.now()); date = date.plusDays(1)) {
                if (stepsEveryDay || random.nextFloat() < 0.8) {
                    Day d = new Day(date, (random.nextInt(stepFactor) * random.nextInt(100) + 100 * stepFactor), u);
                    days.add(d);
                    System.out.print(d.getSteps() + " ");
                } else {
                    System.out.print("0 ");
                }
            }
            User current_u = userService.createAndRegisterIfNotExist(u);
            dayRepository.saveAll(days);
            current_u.setDays(days);
            userRepository.save(current_u);

        }

    }

    private void createLiveData() throws EntityNotFoundException {
        competitionService.updateCompetition(new UpdateCompetitionDTO(LocalDate.now().minusDays(13), LocalDate.now().plusDays(25), false));
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
            users.add(new User("user" + i + "@gericht-brb.dummy", (i == 0) || (i != 1 && random.nextFloat() < 0.1)));
        }
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            u.setGroup((i < 3) ? groups.get(0) : ((i >= users.size() - 2) ? groups.get(groups.size() - 1) : groups.get(random.nextInt(groups.size() - 1))));
            u.setPassword("12345678");
            u.setCompetitionName(AvailableNameService.getAvailableName());
            int stepFactor = (i==0) ? 100 : random.nextInt(80) + 20;
            if (random.nextFloat() < 0.5) {
                int stepGoal = (random.nextInt(100) + 100) * stepFactor;
                u.setStepGoal(stepGoal == 0 ? 10000 : stepGoal);
            }

            System.out.println("\n\nCreated User: " + u);

            boolean stepsEveryDay = i == 0 || random.nextFloat() < 0.5;
            HashSet<Day> days = new HashSet<>();
            for (LocalDate date = competitionService.getCompetition().getStartDate(); !date.isAfter(competitionService.getCompetition().getStartDate().plusDays(6)); date = date.plusDays(1)) {
                if (stepsEveryDay || random.nextFloat() < 0.8) {
                    Day d = new Day(date, (random.nextInt(stepFactor) * random.nextInt(100) + 100 * stepFactor), u);
                    days.add(d);
                    System.out.print(d.getSteps() + " ");
                } else {
                    System.out.print("0 ");
                }
            }
            User current_u = userService.createAndRegisterIfNotExist(u);
            dayRepository.saveAll(days);
            current_u.setDays(days);
            userRepository.save(current_u);

        }

    }

    @SneakyThrows
    private void generateUserNames() {
        AvailableNameService.generateAvailableNames(userAdjectives, userNouns);
    }
}
