package com.example.demo.dtos.pagamento;

import com.example.demo.enums.FormaPagamento;
import com.example.demo.enums.StatusPagamento;
import com.example.demo.enums.StatusPedido;
import com.example.demo.model.Pagamento;

import java.math.BigDecimal;

public record PagamentoResponseDto(Long pagamentoId, Long pedidoId, BigDecimal valor, FormaPagamento formaPagamento,
                                   StatusPagamento statusPagamento, String transacaoId,
                                   StatusPedido statusPedidoAtualizado) {

    public PagamentoResponseDto(Pagamento pagamento) {
        this(pagamento.getPagamentoId(),
                pagamento.getPedido().getPedidoId(),
                pagamento.getValor(),
                pagamento.getFormaPagamento(),
                pagamento.getStatus(),
                pagamento.getTransacaoId(),
                pagamento.getPedido().getStatus());
    }
}
