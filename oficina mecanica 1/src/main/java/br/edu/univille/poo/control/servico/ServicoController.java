package br.edu.univille.poo.control.servico;

import br.edu.univille.poo.model.entity.Servico;
import br.edu.univille.poo.model.usecase.ExcluirServicoUseCase;
import br.edu.univille.poo.model.usecase.PesquisarServicosUseCase;
import br.edu.univille.poo.model.usecase.SalvarServicoUseCase;

import java.util.List;

public class ServicoController {

    private PesquisarServicosUseCase pesquisarUseCase;
    private SalvarServicoUseCase salvarUseCase;
    private ExcluirServicoUseCase excluirUseCase;

    public ServicoController() {
        pesquisarUseCase = new PesquisarServicosUseCase();
        salvarUseCase = new SalvarServicoUseCase();
        excluirUseCase = new ExcluirServicoUseCase();
    }

    public List<Servico> pesquisar(String descricao) {
        return pesquisarUseCase.executar(descricao);
    }

    public void salvar(Servico servico) {
        salvarUseCase.executar(servico);
    }

    public void excluir(Integer id) {
        excluirUseCase.executar(id);
    }

}
