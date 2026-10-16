package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ClienteDAO;
import br.edu.univille.poo.model.dao.VeiculoDAO;
import br.edu.univille.poo.model.exception.ClienteComVeiculosException;

public class ExcluirClienteUseCase {

    private ClienteDAO dao;
    private VeiculoDAO veiculoDAO;

    public ExcluirClienteUseCase() {
        dao = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();
    }

    public void executar(Integer clienteId) {
        if (veiculoDAO.existePorCliente(clienteId)) {
            throw new ClienteComVeiculosException();
        }
        dao.excluir(clienteId);
    }

}
