package br.edu.univille.poo.model.dao;

import br.edu.univille.poo.model.entity.ItemServico;
import br.edu.univille.poo.model.entity.Servico;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemServicoDAO extends BaseDAO {

    public void inserir(Connection con, Integer ordemServicoId, ItemServico item) throws SQLException {
        String sql = """
            INSERT INTO item_servico (
                ordem_servico_id,
                servico_id,
                quantidade,
                valor_unitario
            ) VALUES (?, ?, ?, ?)
            """;

        try (var ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ordemServicoId);
            ps.setInt(2, item.getServico().getId());
            ps.setInt(3, item.getQuantidade());
            ps.setDouble(4, item.getValorUnitario());

            ps.executeUpdate();

            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        }
    }

    public void excluirPorOrdem(Connection con, Integer ordemServicoId) throws SQLException {
        String sql = """
            DELETE FROM item_servico
            WHERE ordem_servico_id = ?
            """;

        try (var ps = con.prepareStatement(sql)) {
            ps.setInt(1, ordemServicoId);
            ps.executeUpdate();
        }
    }

    public List<ItemServico> obterPorOrdem(Connection con, Integer ordemServicoId) throws SQLException {
        String sql = """
            SELECT
                i.id,
                i.quantidade,
                i.valor_unitario,

                s.id AS servico_id,
                s.descricao AS servico_descricao,
                s.valor AS servico_valor

            FROM item_servico i
            JOIN servico s ON s.id = i.servico_id
            WHERE i.ordem_servico_id = ?
            ORDER BY i.id
            """;

        var lista = new ArrayList<ItemServico>();

        try (var ps = con.prepareStatement(sql)) {

            ps.setInt(1, ordemServicoId);

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var servico = new Servico();
                    servico.setId(rs.getInt("servico_id"));
                    servico.setDescricao(rs.getString("servico_descricao"));
                    servico.setValor(rs.getDouble("servico_valor"));

                    var item = new ItemServico();
                    item.setId(rs.getInt("id"));
                    item.setServico(servico);
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setValorUnitario(rs.getDouble("valor_unitario"));

                    lista.add(item);
                }
            }
        }

        return lista;
    }

    public boolean existePorServico(Integer servicoId) {
        String sql = """
            SELECT COUNT(*)
            FROM item_servico
            WHERE servico_id = ?
            """;

        try (var con = connection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, servicoId);

            try (var rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
