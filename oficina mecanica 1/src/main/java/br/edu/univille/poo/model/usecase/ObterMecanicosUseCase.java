package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.MecanicoDAO;
import br.edu.univille.poo.model.entity.Mecanico;

import java.util.List;

public class ObterMecanicosUseCase {

    private MecanicoDAO dao;

    public ObterMecanicosUseCase() {
        dao = new MecanicoDAO();
    }

    public List<Mecanico> executar() {
        return dao.obterTodos();
    }

}
