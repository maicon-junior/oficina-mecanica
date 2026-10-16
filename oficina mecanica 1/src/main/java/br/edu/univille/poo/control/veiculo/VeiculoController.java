package br.edu.univille.poo.control.veiculo;

import br.edu.univille.poo.model.entity.Cliente;
import br.edu.univille.poo.model.entity.Veiculo;
import br.edu.univille.poo.model.usecase.ExcluirVeiculoUseCase;
import br.edu.univille.poo.model.usecase.ObterClientesUseCase;
import br.edu.univille.poo.model.usecase.PesquisarVeiculosUseCase;
import br.edu.univille.poo.model.usecase.SalvarVeiculoUseCase;

import java.util.List;

public class VeiculoController {

    private PesquisarVeiculosUseCase pesquisarUseCase;
    private SalvarVeiculoUseCase salvarUseCase;
    private ExcluirVeiculoUseCase excluirUseCase;
    private ObterClientesUseCase clientesUseCase;

    public VeiculoController() {
        pesquisarUseCase = new PesquisarVeiculosUseCase();
        salvarUseCase = new SalvarVeiculoUseCase();
        excluirUseCase = new ExcluirVeiculoUseCase();
        clientesUseCase = new ObterClientesUseCase();
    }

    public List<Veiculo> pesquisar(String placa) {
        return pesquisarUseCase.executar(placa);
    }

    public void salvar(Veiculo veiculo) {
        salvarUseCase.executar(veiculo);
    }

    public void excluir(Integer id) {
        excluirUseCase.executar(id);
    }

    public List<Cliente> obterClientes() {
        return clientesUseCase.executar();
    }

}
