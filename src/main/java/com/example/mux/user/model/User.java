package com.example.mux.user.model;

import com.example.mux.day.model.Day;
import com.example.mux.group.model.Group;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    private List<Day> days;

    @ManyToOne
    @JoinColumn(name = "group_ID")
    private Group group;

    public User(String email, boolean isAdmin){
        this.isAdmin = isAdmin;
        this.email = email;
    }

    public void setPassword(String password){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
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

}