package com.example.mux.group.service;

import com.example.mux.exception.EntityNotFoundException;
import com.example.mux.group.model.Group;
import com.example.mux.group.model.dto.GroupCreationDTO;
import com.example.mux.group.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public List<Group> createGroups(List<GroupCreationDTO> groupCreations){
        List<Group> groups = new LinkedList<>();
        groupCreations.forEach(groupCreation -> groups.add(new Group(groupCreation.getName(), groupCreation.getNumberOfEmployees())));
        return groupRepository.saveAll(groups);
    }

    public void deleteGroup(int ID){
        groupRepository.deleteById(ID);
    }

    public Group updateGroup(int ID, GroupCreationDTO groupCreation) throws EntityNotFoundException {
        Group group = groupRepository.findByID(ID).orElseThrow(()->new EntityNotFoundException("There is no group with this ID."));
        group.setName(groupCreation.getName());
        group.setNumberOfEmployees(groupCreation.getNumberOfEmployees());
        return groupRepository.save(group);
    }

    public Group getGroup(int ID) throws EntityNotFoundException {
        return groupRepository.findByID(ID).orElseThrow(()->new EntityNotFoundException("There is no group with this ID."));
    }

    public Group getGroup(String name)throws EntityNotFoundException {
        return groupRepository.findByName(name).orElseThrow(()->new EntityNotFoundException("There is no group with this name."));
    }

    public List<Group> getGroups(){
        return groupRepository.findAll();
    }

    public boolean groupExists(String name){
        return groupRepository.existsByName(name);
    }

    public Group createIfNotExist(Group group){
        Optional<Group> groupOptional = groupRepository.findByName(group.getName());
        if(groupOptional.isPresent()){
            return groupOptional.get();
        }else{
            return groupRepository.save(group);
        }
    }
}
