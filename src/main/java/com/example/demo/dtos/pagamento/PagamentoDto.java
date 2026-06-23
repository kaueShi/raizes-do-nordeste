package com.example.demo.dtos.pagamento;

import com.example.demo.enums.FormaPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoDto(@NotNull FormaPagamento formaPagamento,
                           Boolean simularFalha) {
}
