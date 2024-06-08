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

    @OneToMany(mappedBy = "group")
    private List<User> users;

    public Group(String name){
        this(0, name);
    }

    public Group(int ID, String name){
        setID(ID);
        setName(name);
        setUsers(new LinkedList<>());
    }
}
