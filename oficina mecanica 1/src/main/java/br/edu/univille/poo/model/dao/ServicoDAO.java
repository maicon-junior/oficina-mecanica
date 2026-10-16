package br.edu.univille.poo.model.dao;

import br.edu.univille.poo.model.entity.Servico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoDAO extends BaseDAO {

    public Optional<Servico> obterPeloId(Integer id) {
        String sql = """
                SELECT id, descricao, valor
                FROM servico
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

    public void inserir(Servico servico) {
        String sql = """
                INSERT INTO servico (descricao, valor)
                VALUES (?, ?)
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, servico.getDescricao());
            comando.setDouble(2, servico.getValor());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    servico.setId(resultado.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Servico servico) {
        String sql = """
                UPDATE servico
                SET descricao = ?, valor = ?
                WHERE id = ?
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, servico.getDescricao());
            comando.setDouble(2, servico.getValor());
            comando.setInt(3, servico.getId());

            comando.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Servico> obterTodosPelaDescricao(String descricao) {
        String sql = """
                SELECT id, descricao, valor
                FROM servico
                WHERE descricao LIKE ?
                ORDER BY descricao
                """;

        List<Servico> servicos = new ArrayList<>();

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, "%" + descricao + "%");

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    servicos.add(montar(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return servicos;
    }

    public List<Servico> obterTodos() {
        return obterTodosPelaDescricao("");
    }

    public void excluir(Integer id) {
        String sql = """
                DELETE FROM servico
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

    private Servico montar(ResultSet resultado) throws SQLException {
        Servico servico = new Servico();

        servico.setId(resultado.getInt("id"));
        servico.setDescricao(resultado.getString("descricao"));
        servico.setValor(resultado.getDouble("valor"));

        return servico;
    }
}
