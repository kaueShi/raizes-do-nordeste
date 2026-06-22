package com.example.demo.dtos;

import com.example.demo.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateDto(@NotNull StatusPedido status) {
}
