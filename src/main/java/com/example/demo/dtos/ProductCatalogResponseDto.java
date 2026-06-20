package com.example.demo.dtos;

import com.example.demo.model.Product;

public record ProductCatalogResponseDto(Long produtoId, String nome, String descricao) {
    public ProductCatalogResponseDto(Product produto) {
        this(produto.getProdutoId(), produto.getNome(), produto.getDescricao());
    }
}
