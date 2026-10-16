package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.MecanicoDAO;
import br.edu.univille.poo.model.entity.Mecanico;

import java.util.List;

public class PesquisarMecanicosUseCase {

    private MecanicoDAO dao;

    public PesquisarMecanicosUseCase() {
        dao = new MecanicoDAO();
    }

    public List<Mecanico> executar(String nome) {
        return dao.obterTodosPeloNome(nome);
    }

}
