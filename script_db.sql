-- ===========================
-- Tabela: operador
-- ===========================
DROP TABLE IF EXISTS operador CASCADE;

CREATE TABLE operador (
    id       BIGSERIAL PRIMARY KEY,
    nome     VARCHAR(255)      NOT NULL,
    login    VARCHAR(100)      NOT NULL,
    senha    VARCHAR(255)      NOT NULL,
    papel    VARCHAR(50)       NOT NULL DEFAULT 'ROLE_USER'
);

-- Evita logins duplicados
CREATE UNIQUE INDEX ux_operador_login ON operador (login);

-- ===========================
-- Tabela: patio
-- ===========================
DROP TABLE IF EXISTS patio CASCADE;

CREATE TABLE patio (
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(255) NOT NULL,
    endereco  VARCHAR(255)
);

-- Nome de pátio não precisa ser único, mas crie se quiser:
-- CREATE UNIQUE INDEX ux_patio_nome ON patio (nome);

-- ===========================
-- Tabela: automovel
-- ===========================
DROP TABLE IF EXISTS automovel CASCADE;

CREATE TABLE automovel (
    id                    BIGSERIAL PRIMARY KEY,
    placa                 VARCHAR(20)   NOT NULL,
    chassi                VARCHAR(50)   NOT NULL,
    tipo                  VARCHAR(100)  NOT NULL,
    cor                   VARCHAR(50),
    localizacao_no_patio  VARCHAR(150),
    comentarios           TEXT,
    patio_id              BIGINT,
    CONSTRAINT fk_automovel_patio
        FOREIGN KEY (patio_id) REFERENCES patio(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- Índices/uniqueness usuais em gestão de veículos
CREATE UNIQUE INDEX ux_automovel_placa  ON automovel (placa);
CREATE UNIQUE INDEX ux_automovel_chassi ON automovel (chassi);

-- Índice útil para consultas por pátio
CREATE INDEX ix_automovel_patio_id ON automovel (patio_id);
