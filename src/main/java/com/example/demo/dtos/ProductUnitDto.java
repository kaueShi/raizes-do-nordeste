package com.example.demo.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUnitDto(Long produtoId, BigDecimal preco, Integer quantidade, Boolean disponivel) {

}
