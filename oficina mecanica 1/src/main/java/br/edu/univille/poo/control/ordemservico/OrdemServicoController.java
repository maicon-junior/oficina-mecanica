package br.edu.univille.poo.control.ordemservico;

import br.edu.univille.poo.model.entity.Mecanico;
import br.edu.univille.poo.model.entity.OrdemServico;
import br.edu.univille.poo.model.entity.Servico;
import br.edu.univille.poo.model.entity.Veiculo;
import br.edu.univille.poo.model.usecase.*;

import java.util.List;

public class OrdemServicoController {

    private SalvarOrdemServicoUseCase salvarUseCase;
    private ExcluirOrdemServicoUseCase excluirUseCase;
    private ObterOrdemServicoPeloIdUseCase obterUseCase;
    private ObterVeiculosUseCase veiculosUseCase;
    private ObterMecanicosUseCase mecanicosUseCase;
    private ObterServicosUseCase servicosUseCase;

    public OrdemServicoController() {
        salvarUseCase = new SalvarOrdemServicoUseCase();
        excluirUseCase = new ExcluirOrdemServicoUseCase();
        obterUseCase = new ObterOrdemServicoPeloIdUseCase();
        veiculosUseCase = new ObterVeiculosUseCase();
        mecanicosUseCase = new ObterMecanicosUseCase();
        servicosUseCase = new ObterServicosUseCase();
    }

    public OrdemServico obterPeloId(Integer id) {
        return obterUseCase.executar(id);
    }

    public void salvar(OrdemServico ordem) {
        salvarUseCase.executar(ordem);
    }

    public void excluir(Integer id) {
        excluirUseCase.executar(id);
    }

    public List<Veiculo> obterVeiculos() {
        return veiculosUseCase.executar();
    }

    public List<Mecanico> obterMecanicos() {
        return mecanicosUseCase.executar();
    }

    public List<Servico> obterServicos() {
        return servicosUseCase.executar();
    }

}
