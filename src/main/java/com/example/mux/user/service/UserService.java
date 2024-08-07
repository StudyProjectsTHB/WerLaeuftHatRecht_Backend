package com.example.mux.user.service;

import com.example.mux.day.model.Day;
import com.example.mux.day.service.DayService;
import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.service.GroupService;
import com.example.mux.user.UserProperties;
import com.example.mux.user.exception.PasswordMismatchException;
import com.example.mux.user.exception.TokenExpiredException;
import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import com.example.mux.user.model.dto.EmailDTO;
import com.example.mux.user.model.dto.UpdateUserDTO;
import com.example.mux.user.model.dto.UserDTO;
import com.example.mux.user.model.dto.UserPasswordsDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.mux.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DayService dayService;
    private final UserProperties userProperties;
    private final EmailService emailService;
    private final UserTokenService userTokenService;
    private final GroupService groupService;

    public void deleteDaysFromUser(User user, List<Day> days) {
        days.forEach(user.getDays()::remove);
        userRepository.save(user);
    }

    public UserDTO updateUser(int userID, UpdateUserDTO updateUser) throws EntityNotFoundException, IllegalArgumentException {
        User user = getUser(userID);
        if(updateUser.getStepGoal() != null) {
            if (updateUser.getStepGoal() < 0) {
                throw new IllegalArgumentException("The value should be greater than 0.");
            }

            user.setStepGoal(updateUser.getStepGoal());
        }

        if(updateUser.getStepSize() != null || updateUser.getHeight() != null){
            if(updateUser.getStepSize() != null && updateUser.getHeight() != null){
                throw new IllegalArgumentException("The users height and step size could not be set simultaneously.");
            }
            if(updateUser.getHeight() != null){
                user.setHeight(updateUser.getHeight());
                user.setStepSize(null);
            }else{
                user.setStepSize(updateUser.getStepSize());
                user.setHeight(null);
            }
        }

        if(updateUser.getIsAdmin() != null){
            user.setIsAdmin(updateUser.getIsAdmin());
        }

        if(updateUser.getEmail() != null){
            user.setEmail(updateUser.getEmail());
        }

        Integer groupId = updateUser.getGroupId();
        if(groupId != null){
            Group group = groupService.getGroup(groupId);
            user.setGroup(group);
        }

        return new UserDTO(userRepository.save(user));
    }


    public User getUser(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with this email not found."));
    }

    public User getUser(int ID) throws EntityNotFoundException {
        return userRepository.findById(ID).orElseThrow(() -> new EntityNotFoundException("User with this ID not found."));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(int ID) {
        userRepository.deleteById(ID);
    }

    public boolean userExists(String email){
        return userRepository.existsByEmail(email);
    }

    public List<User> getUsers(Group group){
        return userRepository.findAllByGroup(group);
    }

    public User createAndRegisterIfNotExist(User user){
        Optional<User> userOptional = userRepository.getUserByEmail(user.getEmail());
        if(userOptional.isPresent()){
            return userOptional.get();
        }else{
            return userRepository.save(user);
        }
    }

    public void checkActivitiesAndSendReminder(){
        LocalDate now = LocalDate.now();
        LocalDate before = now.minusDays(userProperties.getRemindingDayLimit());
        List<User> users = getUsers();
        for(User user: users){
            if(user.isEnabled()) {
                if (!dayService.existsForUserWitDateBetween(user, before, now)) {
                    emailService.sendReminderEmail(user, userProperties.getRemindingDayLimit());
                }
            }
        }
    }

    public void startPasswordResetProcess(EmailDTO emailDTO) throws EntityNotFoundException {
        User user = userRepository.getUserByEmail(emailDTO.getEmail()).orElseThrow(() -> new EntityNotFoundException("User with this email not found."));
        UserToken userToken = userTokenService.buildUserToken(user);
        userTokenService.saveUserToken(userToken);
        emailService.sendPasswordResetEmail(user, userToken);
    }

    public void resetPassword(UserPasswordsDTO userPasswords,String token) throws EntityNotFoundException, TokenExpiredException, PasswordMismatchException {
        User user = getUserFromToken(userPasswords, token, userTokenService);

        user.setPassword(userPasswords.getPassword());

        userRepository.save(user);
    }

    public User getUserFromToken(UserPasswordsDTO userPasswords, String token, UserTokenService userTokenService) throws EntityNotFoundException, TokenExpiredException, PasswordMismatchException {
        UUID uuid = UUID.fromString(token);
        UserToken userToken = userTokenService.getUserToken(uuid);

        if (LocalDateTime.now().isAfter(userToken.getExpiresAt())) {
            throw new TokenExpiredException("The token is expired.");
        }

        if (!userPasswords.getPassword().equals(userPasswords.getPasswordConfirm())) {
            throw new PasswordMismatchException("The passwords do not match.");
        }

        return userToken.getUser();
    }
}
