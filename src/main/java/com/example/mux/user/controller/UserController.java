package com.example.mux.user.controller;

import com.example.mux.user.exception.PasswordMismatchException;
import com.example.mux.user.exception.TokenExpiredException;
import com.example.mux.user.exception.TokenNotFoundException;
import com.example.mux.user.model.dto.*;
import com.example.mux.user.service.AuthenticationService;
import com.example.mux.user.service.JWTManagerService;
import com.example.mux.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationRequestDTO request) {
        try {
            return ResponseEntity.ok().body(authenticationService.authenticateUser(request));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ResponseBody
    @PutMapping("register/{token}")
    public ResponseEntity<UserDTO> registerUserPassword(@RequestBody UserPasswordsDTO userPasswords, @PathVariable String token){
        try {
            UserDTO userDTO = new UserDTO(authenticationService.registerUser(userPasswords, token));
            return ResponseEntity.ok(userDTO);
        } catch (TokenNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (PasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //TODO add logout

    @ResponseBody
    @PostMapping
    public List<UserTokenDTO> createUsers(@RequestBody List<UserCreationDTO> userCreations){
        return UserTokenDTO.fromUserTokenList(authenticationService.createUsers(userCreations));
    }

    @GetMapping
    public List<UserDTO> getUsers(){
        List<UserDTO> users = UserDTO.fromUserList(userService.getUsers());;
        return users;
    }

    @DeleteMapping("/{ID}")
    public void deleteUser(@PathVariable int ID){
        userService.deleteUser(ID);
    }

}
