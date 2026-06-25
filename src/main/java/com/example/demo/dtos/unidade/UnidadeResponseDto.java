package com.example.demo.dtos.unidade;

import com.example.demo.model.Unidade;

public record UnidadeResponseDto(Long unidadeId, String nome, String cidade,
                                 String uf, boolean cozinhaCompleta) {

    public UnidadeResponseDto(Unidade unidade){
        this(unidade.getUnidadeId(), unidade.getNome(), unidade.getCidade(),
                unidade.getUf(), unidade.isCozinhaCompleta());
    }
}
