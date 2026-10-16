package br.edu.univille.poo.model.dao;

import br.edu.univille.poo.model.entity.Cliente;
import br.edu.univille.poo.model.entity.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VeiculoDAO extends BaseDAO {

    private static final String SELECT = """
            SELECT
                v.id,
                v.placa,
                v.marca,
                v.modelo,
                v.ano,
                v.cor,

                c.id AS cliente_id,
                c.nome AS cliente_nome,
                c.cpf AS cliente_cpf,
                c.telefone AS cliente_telefone,
                c.email AS cliente_email,
                c.endereco AS cliente_endereco

            FROM veiculo v
            JOIN cliente c ON c.id = v.cliente_id
            """;

    public Optional<Veiculo> obterPeloId(Integer id) {
        String sql = SELECT + " WHERE v.id = ?";

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(montar(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public Optional<Veiculo> obterPelaPlaca(String placa) {
        String sql = SELECT + " WHERE v.placa = ?";

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, placa);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(montar(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public List<Veiculo> obterTodosPelaPlaca(String placa) {
        String sql = SELECT + " WHERE v.placa LIKE ? ORDER BY v.placa";

        var lista = new ArrayList<Veiculo>();

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + placa + "%");

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public List<Veiculo> obterTodos() {
        return obterTodosPelaPlaca("");
    }

    public void inserir(Veiculo veiculo) {
        String sql = """
                INSERT INTO veiculo (placa, marca, modelo, ano, cor, cliente_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (var con = connection();
             var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencher(ps, veiculo);
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    veiculo.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Veiculo veiculo) {
        String sql = """
                UPDATE veiculo SET
                    placa = ?,
                    marca = ?,
                    modelo = ?,
                    ano = ?,
                    cor = ?,
                    cliente_id = ?
                WHERE id = ?
                """;

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            preencher(ps, veiculo);
            ps.setInt(7, veiculo.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existePorCliente(Integer clienteId) {
        String sql = """
            SELECT COUNT(*)
            FROM veiculo
            WHERE cliente_id = ?
            """;

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, clienteId);

            try (var rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int contar() {
        try (var con = connection();
             var ps = con.prepareStatement("SELECT COUNT(*) FROM veiculo");
             var rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Integer id) {
        String sql = """
                DELETE FROM veiculo
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

    private void preencher(PreparedStatement ps, Veiculo veiculo) throws SQLException {
        ps.setString(1, veiculo.getPlaca());
        ps.setString(2, veiculo.getMarca());
        ps.setString(3, veiculo.getModelo());
        ps.setInt(4, veiculo.getAno());
        ps.setString(5, veiculo.getCor());
        ps.setInt(6, veiculo.getCliente().getId());
    }

    private Veiculo montar(ResultSet rs) throws SQLException {
        var cliente = new Cliente();
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setCpf(rs.getString("cliente_cpf"));
        cliente.setTelefone(rs.getString("cliente_telefone"));
        cliente.setEmail(rs.getString("cliente_email"));
        cliente.setEndereco(rs.getString("cliente_endereco"));

        var veiculo = new Veiculo();
        veiculo.setId(rs.getInt("id"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setCor(rs.getString("cor"));
        veiculo.setCliente(cliente);

        return veiculo;
    }
}
