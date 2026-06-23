/*DROP TABLE IF EXISTS tb_user CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
*/
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tb_usuario
(
    usuario_id    UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    nome   VARCHAR(255) NOT NULL,
    senha   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL,

    CONSTRAINT uc_tb_usuario_email UNIQUE (email),
    CONSTRAINT uc_tb_usuario_nome UNIQUE (nome),
    CONSTRAINT chk_role CHECK (role IN ('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_FUNCIONARIO'))
);