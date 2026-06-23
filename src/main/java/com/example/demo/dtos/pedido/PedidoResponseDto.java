package com.example.demo.dtos.pedido;

import com.example.demo.enums.CanalPedido;
import com.example.demo.enums.StatusPedido;
import com.example.demo.model.Pedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDto(Long pedidoId, UUID clienteId, String clienteNome, Long unidadeId, String unidadeNome,
                                CanalPedido canalPedido, StatusPedido status, BigDecimal total,
                                List<ItemPedidoResponseDto> itens, Instant createdAt) {

    public PedidoResponseDto(Pedido pedido) {
        this(pedido.getPedidoId(),
                pedido.getCliente().getUsuarioId(),
                pedido.getCliente().getUsername(),
                pedido.getUnidadePedido().getUnidadeId(),
                pedido.getUnidadePedido().getNome(),
                pedido.getCanalPedido(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getItens().stream().map(ItemPedidoResponseDto::new).toList(),
                pedido.getDataPedido() != null ? pedido.getDataPedido().toInstant(java.time.ZoneOffset.UTC) : null);
    }
}
