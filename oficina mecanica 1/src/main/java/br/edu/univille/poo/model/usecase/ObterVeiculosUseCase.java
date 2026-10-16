package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.VeiculoDAO;
import br.edu.univille.poo.model.entity.Veiculo;

import java.util.List;

public class ObterVeiculosUseCase {

    private VeiculoDAO dao;

    public ObterVeiculosUseCase() {
        dao = new VeiculoDAO();
    }

    public List<Veiculo> executar() {
        return dao.obterTodos();
    }

}
