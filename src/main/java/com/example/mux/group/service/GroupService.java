package com.example.mux.group.service;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public List<Group> createGroups(List<GroupCreationDTO> groupCreations){
        List<Group> groups = new LinkedList<>();
        groupCreations.forEach(groupCreation -> groups.add(new Group(groupCreation.getName())));
        return groupRepository.saveAll(groups);
    }

    public void deleteGroup(int ID){
        groupRepository.deleteById(ID);
    }

    public Group updateGroup(int ID, GroupCreationDTO groupCreation) throws EntityNotFoundException {
        Group group = groupRepository.findByID(ID).orElseThrow(()->new EntityNotFoundException("There is no group with this ID."));
        group.setName(groupCreation.getName());
        return groupRepository.save(group);
    }

    public Group getGroup(int ID) throws EntityNotFoundException {
        return groupRepository.findByID(ID).orElseThrow(()->new EntityNotFoundException("There is no group with this ID."));
    }

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }
}
