package com.example.mux;

import com.example.mux.competition.service.CompetitionService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.service.GroupService;
import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import com.example.mux.user.model.dto.UserCreationDTO;
import com.example.mux.user.service.AuthenticationService;
import com.example.mux.user.service.AvailableNameService;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class StartAppCommandLineRunner implements CommandLineRunner {
    private final InitProperties initProperties;
    private final GroupService groupService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final CompetitionService competitionService;

    @Override
    public void run(String... args) throws Exception {
        competitionService.createInitialCompetition();
        generateUserNames();

        Group group;
        try {
            group = groupService.getGroup(initProperties.getGroupName());
        }catch (EntityNotFoundException e){
            group = groupService.createGroups(Collections.singletonList(new GroupCreationDTO(initProperties.getGroupName()))).get(0);
        }

        if(!userService.userExists(initProperties.getEmail())){
            UserToken userToken = authenticationService.createUsers(Collections.singletonList(new UserCreationDTO(initProperties.getEmail(), true, group.getID()))).get(0);
            System.out.println(userToken.getToken());
        }

        createTestData();

    }

    private void createTestData(){

        Group group1 = groupService.createIfNotExist(new Group("OLG Brandenburg"));
        Group group2 = groupService.createIfNotExist(new Group("AG Potsdam"));
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

    private void generateUserNames(){
        final List<String> adjectives = Arrays.asList("schneller", "schöner", "perfekter", "attraktiver", "fleißiger", "eifriger", "herzlicher", "glücklicher", "lieber", "lustiger", "selbstbewusster", "sympathischer", "starker", "stolzer", "toller", "überragender", "vorbildlicher", "wendiger", "zauberhafter");
        final List<String> nouns = Arrays.asList("Löwe", "Pinguin", "Elch", "Hase", "Igel", "Tiger", "Wolf");
        AvailableNameService.generateAvailableNames(adjectives, nouns);
    }
}
