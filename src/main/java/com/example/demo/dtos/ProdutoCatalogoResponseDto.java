package com.example.demo.dtos;

import com.example.demo.model.Produto;

public record ProdutoCatalogoResponseDto(Long produtoId, String nome, String descricao) {
    public ProdutoCatalogoResponseDto(Produto produto) {
        this(produto.getProdutoId(), produto.getNome(), produto.getDescricao());
    }
}
