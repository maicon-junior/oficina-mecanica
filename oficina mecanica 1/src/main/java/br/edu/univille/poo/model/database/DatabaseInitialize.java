package br.edu.univille.poo.model.database;

import br.edu.univille.poo.model.dao.ConnectionFactory;

import java.sql.SQLException;

public class DatabaseInitialize {

    public static void init() {
        String inserirClientes = """
        INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES
        ('João da Silva', '123.456.789-00', '(47) 99999-1111', 'joao@email.com', 'Rua das Flores, 100'),
        ('Maria Oliveira', '987.654.321-00', '(47) 98888-2222', 'maria@email.com', 'Av. Brasil, 250'),
        ('Carlos Souza', '111.222.333-44', '(47) 97777-3333', 'carlos@email.com', 'Rua XV de Novembro, 45')
        """;

        String inserirVeiculos = """
        INSERT INTO veiculo (placa, marca, modelo, ano, cor, cliente_id) VALUES
        ('ABC1D23', 'Volkswagen', 'Gol', 2015, 'Prata', 1),
        ('XYZ4E56', 'Fiat', 'Uno', 2012, 'Branco', 2),
        ('QWE7R89', 'Chevrolet', 'Onix', 2020, 'Preto', 2),
        ('RTY2T34', 'Ford', 'Ka', 2018, 'Vermelho', 3)
        """;

        String inserirMecanicos = """
        INSERT INTO mecanico (nome, cpf, telefone, especialidade) VALUES
        ('Pedro Santos', '222.333.444-55', '(47) 96666-4444', 'Motor'),
        ('Lucas Pereira', '333.444.555-66', '(47) 95555-5555', 'Suspensão e freios')
        """;

        String inserirServicos = """
        INSERT INTO servico (descricao, valor) VALUES
        ('Troca de óleo', 120.00),
        ('Alinhamento e balanceamento', 150.00),
        ('Troca de pastilhas de freio', 200.00),
        ('Revisão geral', 350.00),
        ('Troca de amortecedor (unidade)', 280.00)
        """;

        String inserirOrdens = """
        INSERT INTO ordem_servico
            (data_abertura, data_conclusao, status, observacao, veiculo_id, mecanico_id)
        VALUES
            (CURRENT_DATE - INTERVAL 5 DAY, CURRENT_DATE - INTERVAL 4 DAY, 'CONCLUIDA', 'Revisão antes de viagem', 1, 1),
            (CURRENT_DATE - INTERVAL 1 DAY, NULL, 'EM_ANDAMENTO', 'Barulho na suspensão dianteira', 3, 2),
            (CURRENT_DATE, NULL, 'ABERTA', 'Troca de óleo periódica', 4, 1)
        """;

        String inserirItens = """
        INSERT INTO item_servico (ordem_servico_id, servico_id, quantidade, valor_unitario) VALUES
        (1, 4, 1, 350.00),
        (1, 1, 1, 120.00),
        (2, 5, 2, 280.00),
        (2, 2, 1, 150.00),
        (3, 1, 1, 120.00)
        """;

        String inserts[] = {inserirClientes, inserirVeiculos, inserirMecanicos,
                inserirServicos, inserirOrdens, inserirItens};

        try (var con = ConnectionFactory.getInstance().get()) {

            try (var comando = con.createStatement();
                 var resultado = comando.executeQuery("SELECT COUNT(*) FROM cliente")) {
                resultado.next();
                if (resultado.getInt(1) > 0) {
                    return;
                }
            }

            for (var insert : inserts) {
                try (var comando = con.createStatement()) {
                    comando.execute(insert);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
