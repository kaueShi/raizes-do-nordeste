package com.example.demo.dtos.unidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadeDto(@NotBlank String nome, @NotBlank String cidade,
                         @NotBlank String uf, @NotNull Boolean cozinhaCompleta) {
}
