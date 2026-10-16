package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.MecanicoDAO;
import br.edu.univille.poo.model.entity.Mecanico;
import br.edu.univille.poo.util.StringsUtil;

import java.util.Objects;

public class SalvarMecanicoUseCase {

    private MecanicoDAO dao;

    public SalvarMecanicoUseCase() {
        dao = new MecanicoDAO();
    }

    public Mecanico executar(Mecanico mecanico) {
        if (StringsUtil.isBlank(mecanico.getNome())) {
            throw new IllegalArgumentException("O nome do mecânico é obrigatório.");
        }
        if (mecanico.getNome().length() < 3) {
            throw new IllegalArgumentException("O nome do mecânico deve ter no mínimo 3 letras.");
        }
        if (StringsUtil.somenteNumeros(mecanico.getCpf()).length() != 11) {
            throw new IllegalArgumentException("Informe um CPF com 11 dígitos.");
        }
        var existente = dao.obterPeloCpf(mecanico.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(mecanico.getId())) {
            throw new IllegalArgumentException("Já existe um mecânico com este CPF cadastrado.");
        }
        if (Objects.isNull(mecanico.getId())) {
            dao.inserir(mecanico);
        } else {
            dao.atualizar(mecanico);
        }
        return mecanico;
    }

}
