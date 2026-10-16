package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ItemServicoDAO;
import br.edu.univille.poo.model.dao.ServicoDAO;
import br.edu.univille.poo.model.exception.ServicoEmUsoException;

public class ExcluirServicoUseCase {

    private ServicoDAO dao;
    private ItemServicoDAO itemDAO;

    public ExcluirServicoUseCase() {
        dao = new ServicoDAO();
        itemDAO = new ItemServicoDAO();
    }

    public void executar(Integer servicoId) {
        if (itemDAO.existePorServico(servicoId)) {
            throw new ServicoEmUsoException();
        }
        dao.excluir(servicoId);
    }

}
