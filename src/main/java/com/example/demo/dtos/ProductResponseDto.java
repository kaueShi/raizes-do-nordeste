package com.example.demo.dtos;

import com.example.demo.model.ProductUnit;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto(Long produtoId, String nome, String descricao, BigDecimal preco, Integer quantidade, Boolean disponivel) {
    public ProductResponseDto(ProductUnit produto){
        this(produto.getProduct().getProdutoId(),
                produto.getProduct().getNome(),
                produto.getProduct().getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDisponivel());
    }
}
;