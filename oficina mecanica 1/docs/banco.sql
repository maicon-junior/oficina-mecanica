-- Sistema de Gestão de Oficina Mecânica - script do banco (MySQL)
-- Não é obrigatório rodar: o sistema cria o banco e as tabelas sozinho ao iniciar.

CREATE DATABASE IF NOT EXISTS oficina DEFAULT CHARACTER SET utf8mb4;
USE oficina;

CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS veiculo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(8) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INT,
    cor VARCHAR(30),
    cliente_id INT NOT NULL,

    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS mecanico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    especialidade VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS ordem_servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_abertura DATE NOT NULL,
    data_conclusao DATE,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(255),
    veiculo_id INT NOT NULL,
    mecanico_id INT NOT NULL,

    FOREIGN KEY (veiculo_id) REFERENCES veiculo(id),
    FOREIGN KEY (mecanico_id) REFERENCES mecanico(id)
);

CREATE TABLE IF NOT EXISTS item_servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ordem_servico_id INT NOT NULL,
    servico_id INT NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id),
    FOREIGN KEY (servico_id) REFERENCES servico(id)
);

INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES
('João da Silva', '123.456.789-00', '(47) 99999-1111', 'joao@email.com', 'Rua das Flores, 100'),
('Maria Oliveira', '987.654.321-00', '(47) 98888-2222', 'maria@email.com', 'Av. Brasil, 250'),
('Carlos Souza', '111.222.333-44', '(47) 97777-3333', 'carlos@email.com', 'Rua XV de Novembro, 45');

INSERT INTO veiculo (placa, marca, modelo, ano, cor, cliente_id) VALUES
('ABC1D23', 'Volkswagen', 'Gol', 2015, 'Prata', 1),
('XYZ4E56', 'Fiat', 'Uno', 2012, 'Branco', 2),
('QWE7R89', 'Chevrolet', 'Onix', 2020, 'Preto', 2),
('RTY2T34', 'Ford', 'Ka', 2018, 'Vermelho', 3);

INSERT INTO mecanico (nome, cpf, telefone, especialidade) VALUES
('Pedro Santos', '222.333.444-55', '(47) 96666-4444', 'Motor'),
('Lucas Pereira', '333.444.555-66', '(47) 95555-5555', 'Suspensão e freios');

INSERT INTO servico (descricao, valor) VALUES
('Troca de óleo', 120.00),
('Alinhamento e balanceamento', 150.00),
('Troca de pastilhas de freio', 200.00),
('Revisão geral', 350.00),
('Troca de amortecedor (unidade)', 280.00);

INSERT INTO ordem_servico
    (data_abertura, data_conclusao, status, observacao, veiculo_id, mecanico_id)
VALUES
    (CURRENT_DATE - INTERVAL 5 DAY, CURRENT_DATE - INTERVAL 4 DAY, 'CONCLUIDA', 'Revisão antes de viagem', 1, 1),
    (CURRENT_DATE - INTERVAL 1 DAY, NULL, 'EM_ANDAMENTO', 'Barulho na suspensão dianteira', 3, 2),
    (CURRENT_DATE, NULL, 'ABERTA', 'Troca de óleo periódica', 4, 1);

INSERT INTO item_servico (ordem_servico_id, servico_id, quantidade, valor_unitario) VALUES
(1, 4, 1, 350.00),
(1, 1, 1, 120.00),
(2, 5, 2, 280.00),
(2, 2, 1, 150.00),
(3, 1, 1, 120.00);
