package com.example.demo.dtos.produto;

import java.math.BigDecimal;

public record ProdutoUnidadeDto(Long produtoId, BigDecimal preco, Integer quantidade, Boolean disponivel) {

}
