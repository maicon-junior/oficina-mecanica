package br.edu.univille.poo.model.dao;

import br.edu.univille.poo.model.entity.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO extends BaseDAO {

    public Optional<Cliente> obterPeloId(Integer id) {
        String sql = """
                SELECT id, nome, cpf, telefone, email, endereco
                FROM cliente
                WHERE id = ?
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(montar(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public Optional<Cliente> obterPeloCpf(String cpf) {
        String sql = """
                SELECT id, nome, cpf, telefone, email, endereco
                FROM cliente
                WHERE cpf = ?
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, cpf);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(montar(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public void inserir(Cliente cliente) {
        String sql = """
                INSERT INTO cliente (nome, cpf, telefone, email, endereco)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getCpf());
            comando.setString(3, cliente.getTelefone());
            comando.setString(4, cliente.getEmail());
            comando.setString(5, cliente.getEndereco());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    cliente.setId(resultado.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = """
                UPDATE cliente
                SET nome = ?, cpf = ?, telefone = ?, email = ?, endereco = ?
                WHERE id = ?
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, cliente.getNome());
            comando.setString(2, cliente.getCpf());
            comando.setString(3, cliente.getTelefone());
            comando.setString(4, cliente.getEmail());
            comando.setString(5, cliente.getEndereco());
            comando.setInt(6, cliente.getId());

            comando.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> obterTodosPeloNome(String nome) {
        String sql = """
                SELECT id, nome, cpf, telefone, email, endereco
                FROM cliente
                WHERE nome LIKE ?
                ORDER BY nome
                """;

        List<Cliente> clientes = new ArrayList<>();

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, "%" + nome + "%");

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    clientes.add(montar(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clientes;
    }

    public List<Cliente> obterTodos() {
        return obterTodosPeloNome("");
    }

    public int contar() {
        String sql = "SELECT COUNT(*) FROM cliente";

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            resultado.next();
            return resultado.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Integer id) {
        String sql = """
                DELETE FROM cliente
                WHERE id = ?
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, id);

            comando.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Cliente montar(ResultSet resultado) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setId(resultado.getInt("id"));
        cliente.setNome(resultado.getString("nome"));
        cliente.setCpf(resultado.getString("cpf"));
        cliente.setTelefone(resultado.getString("telefone"));
        cliente.setEmail(resultado.getString("email"));
        cliente.setEndereco(resultado.getString("endereco"));

        return cliente;
    }
}
