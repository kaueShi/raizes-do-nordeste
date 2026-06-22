package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoDto(@NotNull Long produtoId, @NotNull @Positive Integer quantidade) {

}

