package com.example.demo.dtos.produto;

import com.example.demo.model.ProdutoUnidade;

import java.math.BigDecimal;

public record CardapioResponseDto (Long produtoUnidadeId, String nome, String descricao, BigDecimal preco, int quantidade, boolean disponivel) {

    public CardapioResponseDto(ProdutoUnidade produtoUnidade) {
        this(produtoUnidade.getProdutoUnidadeId(),
                produtoUnidade.getProduto().getNome(),
                produtoUnidade.getProduto().getDescricao(),
                produtoUnidade.getPreco(),
                produtoUnidade.getQuantidade(),
                produtoUnidade.getDisponivel());
    }

}