package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ServicoDAO;
import br.edu.univille.poo.model.entity.Servico;
import br.edu.univille.poo.util.StringsUtil;

import java.util.Objects;

public class SalvarServicoUseCase {

    private ServicoDAO dao;

    public SalvarServicoUseCase() {
        dao = new ServicoDAO();
    }

    public Servico executar(Servico servico) {
        if (StringsUtil.isBlank(servico.getDescricao())) {
            throw new IllegalArgumentException("A descrição do serviço é obrigatória.");
        }
        if (servico.getDescricao().length() < 3) {
            throw new IllegalArgumentException("A descrição do serviço deve ter no mínimo 3 letras.");
        }
        if (servico.getValor() <= 0) {
            throw new IllegalArgumentException("O valor do serviço deve ser maior que zero.");
        }
        for (var s : dao.obterTodosPelaDescricao(servico.getDescricao())) {
            if (s.getDescricao().equalsIgnoreCase(servico.getDescricao()) && !s.getId().equals(servico.getId())) {
                throw new IllegalArgumentException("Já existe um serviço com esta descrição cadastrado.");
            }
        }
        if (Objects.isNull(servico.getId())) {
            dao.inserir(servico);
        } else {
            dao.atualizar(servico);
        }
        return servico;
    }

}
