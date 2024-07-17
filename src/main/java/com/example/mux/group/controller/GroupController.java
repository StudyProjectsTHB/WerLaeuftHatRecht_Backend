package com.example.mux.group.controller;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.model.dto.GroupDTO;
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
    public List<GroupDTO> createGroups(@RequestBody List<GroupCreationDTO> groupCreations){
        return GroupDTO.fromGroupList(groupService.createGroups(groupCreations));
    }

    @DeleteMapping("/{ID}")
    public void deleteGroup(@PathVariable int ID){
        groupService.deleteGroup(ID);
    }

    @PutMapping("/{ID}")
    @ResponseBody
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable int ID, @RequestBody GroupCreationDTO groupCreation){
        try {
            return ResponseEntity.ok(new GroupDTO(groupService.updateGroup(ID, groupCreation)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{ID}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable int ID){
        try {
            return ResponseEntity.ok(new GroupDTO(groupService.getGroup(ID)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public List<GroupDTO> getGroups(){
        return GroupDTO.fromGroupList(groupService.getGroups());
    }
}
