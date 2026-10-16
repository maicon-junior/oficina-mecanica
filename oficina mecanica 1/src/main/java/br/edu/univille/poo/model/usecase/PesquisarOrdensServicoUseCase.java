package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.entity.OrdemServico;
import br.edu.univille.poo.model.entity.StatusOrdem;

import java.util.List;

public class PesquisarOrdensServicoUseCase {

    private OrdemServicoDAO dao;

    public PesquisarOrdensServicoUseCase() {
        dao = new OrdemServicoDAO();
    }

    public List<OrdemServico> executar(StatusOrdem status, String placa) {
        return dao.pesquisar(status, placa);
    }

}
