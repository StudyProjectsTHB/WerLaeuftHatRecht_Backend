package com.example.mux.user.model;

import com.example.mux.challenge.model.Challenge;
import com.example.mux.day.model.Day;
import com.example.mux.group.model.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true, length = 64)
    private String password;

    @Column(nullable = true)
    private boolean isAdmin;

    @Column(nullable = true)
    private String noun;

    @Column(nullable = true)
    private String adjective;

    @Column(nullable = false)
    private int stepGoal;

    private Integer height;

    private Integer stepSize;

    @ManyToOne
    @JoinColumn(name = "group_ID")
    private Group group;

    @OneToMany
    private Set<Day> days;

    public User(String email, boolean isAdmin){
        this.isAdmin = isAdmin;
        this.email = email;
        setHeight(0);
        setStepGoal(10000);
    }

    public void setPassword(String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setCompetitionName(Pair<String, String> adjective_noun) {
        setAdjective(adjective_noun.getFirst());
        setNoun(adjective_noun.getSecond());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "USER";
        if(isAdmin){
            roleName = "ADMIN";
        }
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return password != null;
    }

    public String getCompetitionUserName(){
        return adjective + " " + noun;
    }

    public String toString() {
        return String.join("", getAdjective(), " ", getNoun(), " - ", getEmail(), " in Group: ", getGroup().getName(), " with step goal: ", Integer.toString(getStepGoal()), "... PW: ", getPassword(), " | isAdmin: ", Boolean.toString(isAdmin));
    }

}