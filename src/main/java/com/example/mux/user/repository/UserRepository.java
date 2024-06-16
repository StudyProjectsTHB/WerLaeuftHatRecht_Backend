package com.example.mux.user.repository;

import com.example.mux.group.model.Group;
import com.example.mux.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    void deleteById(Integer ID);

    boolean existsByEmail(String email);

    List<User> findAllByGroup(Group group);
    Optional<User> getUserByEmail(String email);
}