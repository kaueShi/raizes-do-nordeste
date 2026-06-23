package com.example.demo.dtos;

import com.example.demo.model.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDto(String nomeProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
    public ItemPedidoResponseDto(ItemPedido item) {
        this(item.getProdutoUnidade().getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
    }
}
