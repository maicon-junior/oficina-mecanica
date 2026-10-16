package br.edu.univille.poo.model.dao;

import br.edu.univille.poo.model.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrdemServicoDAO extends BaseDAO {

    private final ItemServicoDAO itemDAO = new ItemServicoDAO();

    private static final String SELECT = """
            SELECT
                os.id,
                os.data_abertura,
                os.data_conclusao,
                os.status,
                os.observacao,

                v.id AS veiculo_id,
                v.placa AS veiculo_placa,
                v.marca AS veiculo_marca,
                v.modelo AS veiculo_modelo,
                v.ano AS veiculo_ano,
                v.cor AS veiculo_cor,

                c.id AS cliente_id,
                c.nome AS cliente_nome,
                c.cpf AS cliente_cpf,
                c.telefone AS cliente_telefone,
                c.email AS cliente_email,
                c.endereco AS cliente_endereco,

                m.id AS mecanico_id,
                m.nome AS mecanico_nome,
                m.cpf AS mecanico_cpf,
                m.telefone AS mecanico_telefone,
                m.especialidade AS mecanico_especialidade

            FROM ordem_servico os
            JOIN veiculo v ON v.id = os.veiculo_id
            JOIN cliente c ON c.id = v.cliente_id
            JOIN mecanico m ON m.id = os.mecanico_id
            """;

    public void salvar(OrdemServico ordem) {
        try (Connection con = connection()) {

            con.setAutoCommit(false);

            try {
                if (Objects.isNull(ordem.getId())) {
                    inserir(con, ordem);
                } else {
                    atualizar(con, ordem);
                    itemDAO.excluirPorOrdem(con, ordem.getId());
                }

                for (ItemServico item : ordem.getItens()) {
                    itemDAO.inserir(con, ordem.getId(), item);
                }

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void inserir(Connection con, OrdemServico ordem) throws SQLException {
        String sql = """
            INSERT INTO ordem_servico (
                data_abertura,
                data_conclusao,
                status,
                observacao,
                veiculo_id,
                mecanico_id
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencher(ps, ordem);
            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ordem.setId(rs.getInt(1));
                }
            }
        }
    }

    private void atualizar(Connection con, OrdemServico ordem) throws SQLException {
        String sql = """
            UPDATE ordem_servico SET
                data_abertura = ?,
                data_conclusao = ?,
                status = ?,
                observacao = ?,
                veiculo_id = ?,
                mecanico_id = ?
            WHERE id = ?
            """;

        try (var ps = con.prepareStatement(sql)) {
            preencher(ps, ordem);
            ps.setInt(7, ordem.getId());
            ps.executeUpdate();
        }
    }

    private void preencher(PreparedStatement ps, OrdemServico ordem) throws SQLException {
        ps.setDate(1, Date.valueOf(ordem.getDataAbertura()));

        if (ordem.getDataConclusao() != null) {
            ps.setDate(2, Date.valueOf(ordem.getDataConclusao()));
        } else {
            ps.setNull(2, Types.DATE);
        }

        ps.setString(3, ordem.getStatus().name());
        ps.setString(4, ordem.getObservacao());
        ps.setInt(5, ordem.getVeiculo().getId());
        ps.setInt(6, ordem.getMecanico().getId());
    }

    public void excluir(Integer id) {
        try (Connection con = connection()) {

            con.setAutoCommit(false);

            try (var ps = con.prepareStatement("DELETE FROM ordem_servico WHERE id = ?")) {

                itemDAO.excluirPorOrdem(con, id);

                ps.setInt(1, id);
                ps.executeUpdate();

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<OrdemServico> obterPeloId(Integer id) {
        String sql = SELECT + " WHERE os.id = ?";

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrdemServico ordem = montar(rs);
                    ordem.setItens(itemDAO.obterPorOrdem(con, ordem.getId()));
                    return Optional.of(ordem);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public List<OrdemServico> pesquisar(StatusOrdem status, String placa) {
        String sql = SELECT + " WHERE v.placa LIKE ?";

        if (status != null) {
            sql += " AND os.status = ?";
        }

        sql += " ORDER BY os.id DESC";

        var lista = new ArrayList<OrdemServico>();

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + placa + "%");

            if (status != null) {
                ps.setString(2, status.name());
            }

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

            for (OrdemServico ordem : lista) {
                ordem.setItens(itemDAO.obterPorOrdem(con, ordem.getId()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public boolean existePorVeiculo(Integer veiculoId) {
        return contar("SELECT COUNT(*) FROM ordem_servico WHERE veiculo_id = ?", veiculoId) > 0;
    }

    public boolean existePorMecanico(Integer mecanicoId) {
        return contar("SELECT COUNT(*) FROM ordem_servico WHERE mecanico_id = ?", mecanicoId) > 0;
    }

    public int contarPorStatus(StatusOrdem status) {
        return contar("SELECT COUNT(*) FROM ordem_servico WHERE status = ?", status.name());
    }

    private int contar(String sql, Object parametro) {
        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setObject(1, parametro);

            try (var rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private OrdemServico montar(ResultSet rs) throws SQLException {
        var cliente = new Cliente();
        cliente.setId(rs.getInt("cliente_id"));
        cliente.setNome(rs.getString("cliente_nome"));
        cliente.setCpf(rs.getString("cliente_cpf"));
        cliente.setTelefone(rs.getString("cliente_telefone"));
        cliente.setEmail(rs.getString("cliente_email"));
        cliente.setEndereco(rs.getString("cliente_endereco"));

        var veiculo = new Veiculo();
        veiculo.setId(rs.getInt("veiculo_id"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setMarca(rs.getString("veiculo_marca"));
        veiculo.setModelo(rs.getString("veiculo_modelo"));
        veiculo.setAno(rs.getInt("veiculo_ano"));
        veiculo.setCor(rs.getString("veiculo_cor"));
        veiculo.setCliente(cliente);

        var mecanico = new Mecanico();
        mecanico.setId(rs.getInt("mecanico_id"));
        mecanico.setNome(rs.getString("mecanico_nome"));
        mecanico.setCpf(rs.getString("mecanico_cpf"));
        mecanico.setTelefone(rs.getString("mecanico_telefone"));
        mecanico.setEspecialidade(rs.getString("mecanico_especialidade"));

        var ordem = new OrdemServico();
        ordem.setId(rs.getInt("id"));
        ordem.setDataAbertura(rs.getDate("data_abertura").toLocalDate());

        Date conclusao = rs.getDate("data_conclusao");
        ordem.setDataConclusao(conclusao != null ? conclusao.toLocalDate() : null);

        ordem.setStatus(StatusOrdem.valueOf(rs.getString("status")));
        ordem.setObservacao(rs.getString("observacao"));
        ordem.setVeiculo(veiculo);
        ordem.setMecanico(mecanico);

        return ordem;
    }
}
