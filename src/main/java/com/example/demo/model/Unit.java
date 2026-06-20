package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_unidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unidadeId;
    private String nome;
    private String cidade;
    private String uf;
    private boolean cozinhaCompleta;


    @OneToMany(mappedBy = "unit")
    private List<ProductUnit> produtos = new ArrayList<>();

}
