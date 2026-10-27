package br.edu.univille.poo.oficina.model.service;

import br.edu.univille.poo.oficina.model.entity.Servico;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.repository.ItemServicoRepository;
import br.edu.univille.poo.oficina.model.repository.ServicoRepository;
import br.edu.univille.poo.oficina.util.StringsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ServicoService {

    private ServicoRepository repository;
    private ItemServicoRepository itemServicoRepository;

    public ServicoService(ServicoRepository repository, ItemServicoRepository itemServicoRepository) {
        this.repository = repository;
        this.itemServicoRepository = itemServicoRepository;
    }

    public List<Servico> obterTodos() {
        return repository.findAllByOrderByDescricaoAsc();
    }

    public List<Servico> pesquisar(String busca) {
        if (StringsUtil.isBlank(busca)) {
            return obterTodos();
        }
        return repository.findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(busca.trim());
    }

    public Servico obterPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado."));
    }

    @Transactional
    public Servico salvar(Servico servico) {
        servico.setDescricao(StringsUtil.limpar(servico.getDescricao()));
        if (StringsUtil.isBlank(servico.getDescricao())) {
            throw new RegraNegocioException("A descrição do serviço é obrigatória.");
        }
        if (servico.getDescricao().length() < 3) {
            throw new RegraNegocioException("A descrição do serviço deve ter no mínimo 3 letras.");
        }
        if (Objects.isNull(servico.getValor()) || servico.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O valor do serviço deve ser maior que zero.");
        }
        var existente = repository.findByDescricaoIgnoreCase(servico.getDescricao());
        if (existente.isPresent() && !existente.get().getId().equals(servico.getId())) {
            throw new RegraNegocioException("Já existe um serviço com esta descrição cadastrado.");
        }
        return repository.save(servico);
    }

    @Transactional
    public void excluir(Long id) {
        obterPeloId(id);
        if (itemServicoRepository.existsByServicoId(id)) {
            throw new RegraNegocioException("O serviço já foi usado em ordens de serviço e não pode ser excluído.");
        }
        repository.deleteById(id);
    }

}
