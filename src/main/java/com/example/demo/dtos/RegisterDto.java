package com.example.demo.dtos;

import com.example.demo.enums.Roles;

public record RegisterDto(String username, String email,String password, Roles role) {
}
