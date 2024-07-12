package com.example.mux.group.model;

import com.example.mux.day.model.Day;
import com.example.mux.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"group\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @NotNull
    private String name;

    private int numberOfEmployees;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new LinkedList<>();

    public Group(String name, int numberOfEmployees){
        this(0, name, numberOfEmployees);
    }

    public Group(int ID, String name, int numberOfEmployees){
        setID(ID);
        setName(name);
        setNumberOfEmployees(numberOfEmployees);
    }
}
