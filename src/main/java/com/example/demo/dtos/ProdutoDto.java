package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;


public record ProdutoDto(@NotBlank String nome, @NotBlank String descricao) {
}
