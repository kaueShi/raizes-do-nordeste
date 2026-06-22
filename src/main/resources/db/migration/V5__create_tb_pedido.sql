CREATE TABLE tb_pedido
(
    pedido_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente_id   UUID NOT NULL REFERENCES tb_user(user_id),
    unidade_id   BIGINT NOT NULL REFERENCES tb_unidade(unidade_id),
    canal_pedido VARCHAR(20) NOT NULL CHECK (canal_pedido IN ('APP','TOTEM','BALCAO','PICKUP','WEB')),
    status       VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO_PAGAMENTO',
    total        NUMERIC(10,2) NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_item_pedido
(
    item_pedido_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id          BIGINT NOT NULL REFERENCES tb_pedido(pedido_id),
    produto_unidade_id BIGINT NOT NULL REFERENCES tb_produto_unidade(produto_unidade_id),
    quantidade         INTEGER NOT NULL,
    preco_unitario     NUMERIC(10,2) NOT NULL
);