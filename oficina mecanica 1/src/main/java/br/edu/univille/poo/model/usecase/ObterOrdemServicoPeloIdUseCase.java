package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.entity.OrdemServico;
import br.edu.univille.poo.model.exception.OrdemServicoNotFoundException;

public class ObterOrdemServicoPeloIdUseCase {

    private OrdemServicoDAO dao;

    public ObterOrdemServicoPeloIdUseCase() {
        dao = new OrdemServicoDAO();
    }

    public OrdemServico executar(Integer id) {
        return dao.obterPeloId(id)
                .orElseThrow(OrdemServicoNotFoundException::new);
    }

}
