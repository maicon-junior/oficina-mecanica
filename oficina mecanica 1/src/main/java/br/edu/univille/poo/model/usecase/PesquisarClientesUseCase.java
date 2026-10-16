package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ClienteDAO;
import br.edu.univille.poo.model.entity.Cliente;

import java.util.List;

public class PesquisarClientesUseCase {

    private ClienteDAO dao;

    public PesquisarClientesUseCase() {
        dao = new ClienteDAO();
    }

    public List<Cliente> executar(String nome) {
        return dao.obterTodosPeloNome(nome);
    }

}
