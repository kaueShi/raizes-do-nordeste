package com.example.demo.dtos.auth;

import com.example.demo.enums.Roles;

public record RegisterDto(String nome, String email, String senha, Roles role) {
}
