CREATE TABLE tb_pagamento
(
    pagamento_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id       BIGINT NOT NULL UNIQUE REFERENCES tb_pedido(pedido_id),
    valor           NUMERIC(10,2) NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    transacao_id    VARCHAR(100) NOT NULL,
    criado_em       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
