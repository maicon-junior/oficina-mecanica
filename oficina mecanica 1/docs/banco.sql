-- Sistema de Gestão de Oficina Mecânica - estrutura do banco (referência)
-- Não é preciso rodar este script: o Spring Data JPA (Hibernate) cria as tabelas
-- a partir das entidades e o arquivo src/main/resources/data.sql insere os dados de exemplo.

CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(150)
);

CREATE TABLE veiculo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(8) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INTEGER,
    cor VARCHAR(30),
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE mecanico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    especialidade VARCHAR(50)
);

CREATE TABLE servico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

CREATE TABLE ordem_servico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_abertura DATE NOT NULL,
    data_conclusao DATE,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(255),
    veiculo_id BIGINT NOT NULL,
    mecanico_id BIGINT NOT NULL,
    FOREIGN KEY (veiculo_id) REFERENCES veiculo(id),
    FOREIGN KEY (mecanico_id) REFERENCES mecanico(id)
);

CREATE TABLE item_servico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ordem_servico_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id),
    FOREIGN KEY (servico_id) REFERENCES servico(id)
);
