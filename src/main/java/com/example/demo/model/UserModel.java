package com.example.demo.model;

import com.example.demo.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;

    public UserModel(String username, String email, String password, Roles role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities(){
        if(this.role == Roles.ROLE_ADMIN){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if(this.role == Roles.ROLE_CLIENTE){
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }
        else return List.of(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return true;
    }
}



