package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ServicoDAO;
import br.edu.univille.poo.model.entity.Servico;

import java.util.List;

public class PesquisarServicosUseCase {

    private ServicoDAO dao;

    public PesquisarServicosUseCase() {
        dao = new ServicoDAO();
    }

    public List<Servico> executar(String descricao) {
        return dao.obterTodosPelaDescricao(descricao);
    }

}
