package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ClienteDAO;
import br.edu.univille.poo.model.entity.Cliente;

import java.util.List;

public class ObterClientesUseCase {

    private ClienteDAO dao;

    public ObterClientesUseCase() {
        dao = new ClienteDAO();
    }

    public List<Cliente> executar() {
        return dao.obterTodos();
    }

}
