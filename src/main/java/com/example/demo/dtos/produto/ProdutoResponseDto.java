package com.example.demo.dtos.produto;

import com.example.demo.model.ProdutoUnidade;

import java.math.BigDecimal;

public record ProdutoResponseDto(Long produtoId, String nome, String descricao, BigDecimal preco, Integer quantidade, Boolean disponivel) {
    public ProdutoResponseDto(ProdutoUnidade produto){
        this(produto.getProduto().getProdutoId(),
                produto.getProduto().getNome(),
                produto.getProduto().getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDisponivel());
    }
}
;