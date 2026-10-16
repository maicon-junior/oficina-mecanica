package br.edu.univille.poo.model.dao;

import br.edu.univille.poo.model.entity.Mecanico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MecanicoDAO extends BaseDAO {

    public Optional<Mecanico> obterPeloId(Integer id) {
        String sql = """
                SELECT id, nome, cpf, telefone, especialidade
                FROM mecanico
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

    public Optional<Mecanico> obterPeloCpf(String cpf) {
        String sql = """
                SELECT id, nome, cpf, telefone, especialidade
                FROM mecanico
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

    public void inserir(Mecanico mecanico) {
        String sql = """
                INSERT INTO mecanico (nome, cpf, telefone, especialidade)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, mecanico.getNome());
            comando.setString(2, mecanico.getCpf());
            comando.setString(3, mecanico.getTelefone());
            comando.setString(4, mecanico.getEspecialidade());

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {
                if (resultado.next()) {
                    mecanico.setId(resultado.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Mecanico mecanico) {
        String sql = """
                UPDATE mecanico
                SET nome = ?, cpf = ?, telefone = ?, especialidade = ?
                WHERE id = ?
                """;

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, mecanico.getNome());
            comando.setString(2, mecanico.getCpf());
            comando.setString(3, mecanico.getTelefone());
            comando.setString(4, mecanico.getEspecialidade());
            comando.setInt(5, mecanico.getId());

            comando.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Mecanico> obterTodosPeloNome(String nome) {
        String sql = """
                SELECT id, nome, cpf, telefone, especialidade
                FROM mecanico
                WHERE nome LIKE ?
                ORDER BY nome
                """;

        List<Mecanico> mecanicos = new ArrayList<>();

        try (Connection conexao = connection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, "%" + nome + "%");

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    mecanicos.add(montar(resultado));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mecanicos;
    }

    public List<Mecanico> obterTodos() {
        return obterTodosPeloNome("");
    }

    public void excluir(Integer id) {
        String sql = """
                DELETE FROM mecanico
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

    private Mecanico montar(ResultSet resultado) throws SQLException {
        Mecanico mecanico = new Mecanico();

        mecanico.setId(resultado.getInt("id"));
        mecanico.setNome(resultado.getString("nome"));
        mecanico.setCpf(resultado.getString("cpf"));
        mecanico.setTelefone(resultado.getString("telefone"));
        mecanico.setEspecialidade(resultado.getString("especialidade"));

        return mecanico;
    }
}
