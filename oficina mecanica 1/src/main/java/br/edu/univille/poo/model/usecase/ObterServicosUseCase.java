package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ServicoDAO;
import br.edu.univille.poo.model.entity.Servico;

import java.util.List;

public class ObterServicosUseCase {

    private ServicoDAO dao;

    public ObterServicosUseCase() {
        dao = new ServicoDAO();
    }

    public List<Servico> executar() {
        return dao.obterTodos();
    }

}
