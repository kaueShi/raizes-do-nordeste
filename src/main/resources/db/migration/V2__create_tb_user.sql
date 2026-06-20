/*DROP TABLE IF EXISTS tb_user CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
*/
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE tb_user
(
    user_id    UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uc_tb_user_email UNIQUE (email),
    CONSTRAINT uc_tb_user_username UNIQUE (username),
    CONSTRAINT chk_role CHECK (role IN ('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_FUNCIONARIO'))
);