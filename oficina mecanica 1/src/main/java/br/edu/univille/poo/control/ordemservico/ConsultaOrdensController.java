package br.edu.univille.poo.control.ordemservico;

import br.edu.univille.poo.model.entity.OrdemServico;
import br.edu.univille.poo.model.entity.StatusOrdem;
import br.edu.univille.poo.model.usecase.PesquisarOrdensServicoUseCase;

import java.util.List;

public class ConsultaOrdensController {

    private PesquisarOrdensServicoUseCase useCase;

    public ConsultaOrdensController() {
        useCase = new PesquisarOrdensServicoUseCase();
    }

    public List<OrdemServico> pesquisar(StatusOrdem status, String placa) {
        return useCase.executar(status, placa);
    }

}
