package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ClienteDAO;
import br.edu.univille.poo.model.entity.Cliente;
import br.edu.univille.poo.util.StringsUtil;

import java.util.Objects;

public class SalvarClienteUseCase {

    private ClienteDAO dao;

    public SalvarClienteUseCase() {
        dao = new ClienteDAO();
    }

    public Cliente executar(Cliente cliente) {
        if (StringsUtil.isBlank(cliente.getNome())) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (cliente.getNome().length() < 3) {
            throw new IllegalArgumentException("O nome do cliente deve ter no mínimo 3 letras.");
        }
        if (StringsUtil.somenteNumeros(cliente.getCpf()).length() != 11) {
            throw new IllegalArgumentException("Informe um CPF com 11 dígitos.");
        }
        var existente = dao.obterPeloCpf(cliente.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Já existe um cliente com este CPF cadastrado.");
        }
        if (Objects.isNull(cliente.getId())) {
            dao.inserir(cliente);
        } else {
            dao.atualizar(cliente);
        }
        return cliente;
    }

}
