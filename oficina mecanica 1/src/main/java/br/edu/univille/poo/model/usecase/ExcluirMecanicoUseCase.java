package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.MecanicoDAO;
import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.exception.MecanicoComOrdensException;

public class ExcluirMecanicoUseCase {

    private MecanicoDAO dao;
    private OrdemServicoDAO ordemDAO;

    public ExcluirMecanicoUseCase() {
        dao = new MecanicoDAO();
        ordemDAO = new OrdemServicoDAO();
    }

    public void executar(Integer mecanicoId) {
        if (ordemDAO.existePorMecanico(mecanicoId)) {
            throw new MecanicoComOrdensException();
        }
        dao.excluir(mecanicoId);
    }

}
