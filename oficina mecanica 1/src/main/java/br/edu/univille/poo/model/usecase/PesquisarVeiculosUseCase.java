package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.VeiculoDAO;
import br.edu.univille.poo.model.entity.Veiculo;

import java.util.List;

public class PesquisarVeiculosUseCase {

    private VeiculoDAO dao;

    public PesquisarVeiculosUseCase() {
        dao = new VeiculoDAO();
    }

    public List<Veiculo> executar(String placa) {
        return dao.obterTodosPelaPlaca(placa);
    }

}
