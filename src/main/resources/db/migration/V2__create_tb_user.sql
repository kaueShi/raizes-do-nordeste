CREATE extension if not exists "pgcrypto";

CREATE TABLE tb_user
(
    user_id       uuid PRIMARY KEY DEFAULT gen_random_uuid()       NOT NULL,
    username VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL
);

ALTER TABLE tb_user
    ADD CONSTRAINT uc_tb_user_email UNIQUE (email);
