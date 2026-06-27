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
@Table(name = "tb_usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID usuarioId;
    private String nome;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private Roles role;

    public Usuario(String nome, String email, String senha, Roles role){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
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



