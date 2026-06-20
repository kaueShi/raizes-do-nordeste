CREATE TABLE tb_produto_unidade
(
    produto_unidade_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    produto_id UUID NOT NULL REFERENCES tb_produtos(produto_id),
    unidade_id UUID NOT NULL REFERENCES tb_unidade(unidade_id),
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 0,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uc_produto_unidade UNIQUE (produto_id, unidade_id)
)

