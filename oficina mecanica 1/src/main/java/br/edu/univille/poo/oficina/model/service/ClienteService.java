package br.edu.univille.poo.oficina.model.service;

import br.edu.univille.poo.oficina.model.entity.Cliente;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.repository.ClienteRepository;
import br.edu.univille.poo.oficina.model.repository.VeiculoRepository;
import br.edu.univille.poo.oficina.util.StringsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private ClienteRepository repository;
    private VeiculoRepository veiculoRepository;

    public ClienteService(ClienteRepository repository, VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
    }

    public List<Cliente> obterTodos() {
        return repository.findAllByOrderByNomeAsc();
    }

    public List<Cliente> pesquisar(String busca) {
        if (StringsUtil.isBlank(busca)) {
            return obterTodos();
        }
        return repository.findByNomeContainingIgnoreCaseOrCpfContainingOrderByNomeAsc(busca.trim(), busca.trim());
    }

    public Cliente obterPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado."));
    }

    public long contar() {
        return repository.count();
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        cliente.setNome(StringsUtil.limpar(cliente.getNome()));
        if (StringsUtil.isBlank(cliente.getNome())) {
            throw new RegraNegocioException("O nome do cliente é obrigatório.");
        }
        if (cliente.getNome().length() < 3) {
            throw new RegraNegocioException("O nome do cliente deve ter no mínimo 3 letras.");
        }
        if (StringsUtil.somenteNumeros(cliente.getCpf()).length() != 11) {
            throw new RegraNegocioException("Informe um CPF com 11 dígitos.");
        }
        cliente.setCpf(StringsUtil.formatarCpf(cliente.getCpf()));
        var existente = repository.findByCpf(cliente.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(cliente.getId())) {
            throw new RegraNegocioException("Já existe um cliente com este CPF cadastrado.");
        }
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        obterPeloId(id);
        if (veiculoRepository.existsByClienteId(id)) {
            throw new RegraNegocioException("O cliente possui veículos cadastrados e não pode ser excluído.");
        }
        repository.deleteById(id);
    }

}
