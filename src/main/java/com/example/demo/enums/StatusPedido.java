package com.example.demo.enums;

public enum StatusPedido {
    AGUARDANDO_PAGAMENTO,
    PAGAMENTO_RECUSADO,
    EM_PREPARO,
    PRONTO,
    ENTREGUE,
    CANCELADO;

    public boolean podeMudarPara(StatusPedido novoStatus) {
        return switch (this) {
            case AGUARDANDO_PAGAMENTO ->
                    novoStatus == EM_PREPARO || novoStatus == PAGAMENTO_RECUSADO || novoStatus == CANCELADO;
            case PAGAMENTO_RECUSADO -> novoStatus == CANCELADO;
            case EM_PREPARO -> novoStatus == PRONTO || novoStatus == CANCELADO;
            case PRONTO -> novoStatus == ENTREGUE || novoStatus == CANCELADO;
            // Se já foi entregue ou cancelado, não pode mudar para mais nada (estado final)
            case ENTREGUE, CANCELADO -> false;
        };
    }
}