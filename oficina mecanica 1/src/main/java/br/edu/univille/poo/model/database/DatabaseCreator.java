package br.edu.univille.poo.model.database;

import br.edu.univille.poo.model.dao.ConnectionFactory;

import java.sql.SQLException;

public class DatabaseCreator {

    public static void create() {
        String tabelaCliente = """
        CREATE TABLE IF NOT EXISTS cliente (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(100) NOT NULL,
            cpf VARCHAR(14) NOT NULL UNIQUE,
            telefone VARCHAR(20),
            email VARCHAR(100),
            endereco VARCHAR(150)
        )
        """;

        String tabelaVeiculo = """
        CREATE TABLE IF NOT EXISTS veiculo (
            id INT AUTO_INCREMENT PRIMARY KEY,
            placa VARCHAR(8) NOT NULL UNIQUE,
            marca VARCHAR(50) NOT NULL,
            modelo VARCHAR(50) NOT NULL,
            ano INT,
            cor VARCHAR(30),
            cliente_id INT NOT NULL,

            FOREIGN KEY (cliente_id) REFERENCES cliente(id)
        )
        """;

        String tabelaMecanico = """
        CREATE TABLE IF NOT EXISTS mecanico (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(100) NOT NULL,
            cpf VARCHAR(14) NOT NULL UNIQUE,
            telefone VARCHAR(20),
            especialidade VARCHAR(50)
        )
        """;

        String tabelaServico = """
        CREATE TABLE IF NOT EXISTS servico (
            id INT AUTO_INCREMENT PRIMARY KEY,
            descricao VARCHAR(100) NOT NULL,
            valor DECIMAL(10,2) NOT NULL
        )
        """;

        String tabelaOrdemServico = """
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
        )
        """;

        String tabelaItemServico = """
        CREATE TABLE IF NOT EXISTS item_servico (
            id INT AUTO_INCREMENT PRIMARY KEY,
            ordem_servico_id INT NOT NULL,
            servico_id INT NOT NULL,
            quantidade INT NOT NULL,
            valor_unitario DECIMAL(10,2) NOT NULL,

            FOREIGN KEY (ordem_servico_id) REFERENCES ordem_servico(id),
            FOREIGN KEY (servico_id) REFERENCES servico(id)
        )
        """;

        String tabelas[] = {tabelaCliente, tabelaVeiculo, tabelaMecanico,
                tabelaServico, tabelaOrdemServico, tabelaItemServico};

        ConnectionFactory factory = ConnectionFactory.getInstance();

        try (var con = factory.getServidor();
             var comando = con.createStatement()) {
            comando.execute("CREATE DATABASE IF NOT EXISTS " + factory.getNomeBanco()
                    + " DEFAULT CHARACTER SET utf8mb4");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        try (var con = factory.get()) {
            for (var tabela : tabelas) {
                try (var comando = con.createStatement()) {
                    comando.execute(tabela);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
