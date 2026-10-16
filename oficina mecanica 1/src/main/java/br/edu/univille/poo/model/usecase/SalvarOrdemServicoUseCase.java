package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.entity.ItemServico;
import br.edu.univille.poo.model.entity.OrdemServico;
import br.edu.univille.poo.model.entity.StatusOrdem;

import java.time.LocalDate;
import java.util.Objects;

public class SalvarOrdemServicoUseCase {

    private OrdemServicoDAO dao;

    public SalvarOrdemServicoUseCase() {
        dao = new OrdemServicoDAO();
    }

    public OrdemServico executar(OrdemServico ordem) {
        if (Objects.isNull(ordem.getVeiculo())) {
            throw new IllegalArgumentException("Selecione o veículo.");
        }
        if (Objects.isNull(ordem.getMecanico())) {
            throw new IllegalArgumentException("Selecione o mecânico responsável.");
        }
        if (Objects.isNull(ordem.getStatus())) {
            throw new IllegalArgumentException("Selecione o status da ordem.");
        }
        if (Objects.isNull(ordem.getDataAbertura())) {
            throw new IllegalArgumentException("Informe a data de abertura.");
        }
        if (ordem.getItens().isEmpty()) {
            throw new IllegalArgumentException("Adicione pelo menos um serviço à ordem.");
        }
        for (ItemServico item : ordem.getItens()) {
            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("A quantidade de cada serviço deve ser maior que zero.");
            }
        }
        if (ordem.getStatus() == StatusOrdem.CONCLUIDA && Objects.isNull(ordem.getDataConclusao())) {
            ordem.setDataConclusao(LocalDate.now());
        }
        if (Objects.nonNull(ordem.getDataConclusao())
                && ordem.getDataConclusao().isBefore(ordem.getDataAbertura())) {
            throw new IllegalArgumentException("A data de conclusão não pode ser anterior à data de abertura.");
        }

        dao.salvar(ordem);
        return ordem;
    }

}
