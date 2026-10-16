package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.exception.OrdemServicoNotFoundException;

public class ExcluirOrdemServicoUseCase {

    private OrdemServicoDAO dao;

    public ExcluirOrdemServicoUseCase() {
        dao = new OrdemServicoDAO();
    }

    public void executar(Integer ordemId) {
        if (dao.obterPeloId(ordemId).isEmpty()) {
            throw new OrdemServicoNotFoundException();
        }
        dao.excluir(ordemId);
    }

}
