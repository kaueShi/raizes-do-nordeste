package com.example.demo.dtos;

import com.example.demo.enums.Roles;
import com.example.demo.model.Usuario;

import java.util.UUID;

public record UsuarioResponseDto(UUID usuarioId, String nome, String email, Roles role) {
    public UsuarioResponseDto(Usuario usuario) {
        this(usuario.getUsuarioId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
    }
}
