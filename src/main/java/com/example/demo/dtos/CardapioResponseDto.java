package com.example.demo.dtos;

import com.example.demo.model.ProductUnit;
import java.math.BigDecimal;

public record CardapioResponseDto (Long produtoUnidadeId, String nome, String descricao, BigDecimal preco, int quantidade, boolean disponivel) {

    public CardapioResponseDto(ProductUnit productUnit) {
        this(productUnit.getProdutoUnidadeId(),
                productUnit.getProduct().getNome(),
                productUnit.getProduct().getDescricao(),
                productUnit.getPreco(),
                productUnit.getQuantidade(),
                productUnit.getDisponivel());
    }

}