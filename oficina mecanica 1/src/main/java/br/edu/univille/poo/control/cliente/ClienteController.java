package br.edu.univille.poo.control.cliente;

import br.edu.univille.poo.model.entity.Cliente;
import br.edu.univille.poo.model.usecase.ExcluirClienteUseCase;
import br.edu.univille.poo.model.usecase.PesquisarClientesUseCase;
import br.edu.univille.poo.model.usecase.SalvarClienteUseCase;

import java.util.List;

public class ClienteController {

    private PesquisarClientesUseCase pesquisarUseCase;
    private SalvarClienteUseCase salvarUseCase;
    private ExcluirClienteUseCase excluirUseCase;

    public ClienteController() {
        pesquisarUseCase = new PesquisarClientesUseCase();
        salvarUseCase = new SalvarClienteUseCase();
        excluirUseCase = new ExcluirClienteUseCase();
    }

    public List<Cliente> pesquisar(String nome) {
        return pesquisarUseCase.executar(nome);
    }

    public void salvar(Cliente cliente) {
        salvarUseCase.executar(cliente);
    }

    public void excluir(Integer id) {
        excluirUseCase.executar(id);
    }

}
