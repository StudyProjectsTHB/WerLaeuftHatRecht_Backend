package com.example.mux;

import com.example.mux.competition.service.CompetitionService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.service.GroupService;
import com.example.mux.user.model.UserToken;
import com.example.mux.user.model.dto.UserCreationDTO;
import com.example.mux.user.service.AuthenticationService;
import com.example.mux.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

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

        Group group = null;
        try {
            group = groupService.getGroup(initProperties.getGroupName());
        }catch (EntityNotFoundException e){
            group = groupService.createGroups(Collections.singletonList(new GroupCreationDTO(initProperties.getGroupName()))).get(0);
        }

        if(!userService.userExists(initProperties.getEmail())){
            UserToken userToken = authenticationService.createUsers(Collections.singletonList(new UserCreationDTO(initProperties.getEmail(), true, group.getID()))).get(0);
            System.out.println(userToken.getToken());
        }


    }
}
