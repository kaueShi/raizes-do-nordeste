package com.example.demo.dtos.pedido;

import com.example.demo.enums.CanalPedido;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoDto(@NotNull Long unidadeId, @NotNull CanalPedido canalPedido,
                        @NotEmpty List<ItemPedidoDto> itens) {
}
