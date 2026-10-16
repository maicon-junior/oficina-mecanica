package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.dao.VeiculoDAO;
import br.edu.univille.poo.model.exception.VeiculoComOrdensException;

public class ExcluirVeiculoUseCase {

    private VeiculoDAO dao;
    private OrdemServicoDAO ordemDAO;

    public ExcluirVeiculoUseCase() {
        dao = new VeiculoDAO();
        ordemDAO = new OrdemServicoDAO();
    }

    public void executar(Integer veiculoId) {
        if (ordemDAO.existePorVeiculo(veiculoId)) {
            throw new VeiculoComOrdensException();
        }
        dao.excluir(veiculoId);
    }

}
