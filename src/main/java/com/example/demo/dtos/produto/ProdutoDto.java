package com.example.demo.dtos.produto;

import jakarta.validation.constraints.NotBlank;


public record ProdutoDto(@NotBlank String nome, @NotBlank String descricao) {
}
