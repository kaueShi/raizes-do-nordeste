CREATE TABLE tb_produtos (
                             produto_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             nome VARCHAR(75) NOT NULL,
                             descricao VARCHAR(255) NOT NULL
);