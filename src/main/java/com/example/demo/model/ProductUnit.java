package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_produto_unidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produtoUnidadeId;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unit unit;

    private BigDecimal preco;
    private int quantidade;
    private boolean disponivel;
}
