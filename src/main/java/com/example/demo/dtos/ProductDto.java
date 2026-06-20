package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;


public record ProductDto(@NotBlank String nome, @NotBlank String descricao) {
}
