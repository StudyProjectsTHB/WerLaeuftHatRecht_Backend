package com.example.mux.group.controller;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @ResponseBody
    public List<Group> createGroups(@RequestBody List<GroupCreationDTO> groupCreations){
        return groupService.createGroups(groupCreations);
    }

    @DeleteMapping("/{ID}")
    public void deleteGroup(@PathVariable int ID){
        groupService.deleteGroup(ID);
    }

    @PutMapping("/{ID}")
    @ResponseBody
    public ResponseEntity<Group> updateGroup(@PathVariable int ID, @RequestBody GroupCreationDTO groupCreation){
        try {
            return ResponseEntity.ok(groupService.updateGroup(ID, groupCreation));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{ID}")
    public ResponseEntity<Group> getGroup(@PathVariable int ID){
        try {
            return ResponseEntity.ok(groupService.getGroup(ID));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public List<Group> getGroups(){
        return groupService.getGroups();
    }
}
