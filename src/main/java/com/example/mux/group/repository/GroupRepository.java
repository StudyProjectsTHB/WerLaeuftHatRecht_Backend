package com.example.mux.group.repository;

import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    void deleteById(Integer ID);
    boolean existsByName(String name);
    Optional<Group> findByName(String name);
    Optional<Group> findByID(int ID);
}
