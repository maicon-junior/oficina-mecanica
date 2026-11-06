package br.edu.univille.poo.oficina.model.service;

import br.edu.univille.poo.oficina.model.entity.Mecanico;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.repository.MecanicoRepository;
import br.edu.univille.poo.oficina.model.repository.OrdemServicoRepository;
import br.edu.univille.poo.oficina.util.StringsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MecanicoService {

    private MecanicoRepository repository;
    private OrdemServicoRepository ordemServicoRepository;

    public MecanicoService(MecanicoRepository repository, OrdemServicoRepository ordemServicoRepository) {
        this.repository = repository;
        this.ordemServicoRepository = ordemServicoRepository;
    }

    public List<Mecanico> obterTodos() {
        return repository.findAllByOrderByNomeAsc();
    }

    public List<Mecanico> pesquisar(String busca) {
        if (StringsUtil.isBlank(busca)) {
            return obterTodos();
        }
        return repository.findByNomeContainingIgnoreCaseOrEspecialidadeContainingIgnoreCaseOrderByNomeAsc(busca.trim(), busca.trim());
    }

    public Mecanico obterPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Mecânico não encontrado."));
    }

    @Transactional
    public Mecanico salvar(Mecanico mecanico) {
        mecanico.setNome(StringsUtil.limpar(mecanico.getNome()));
        if (StringsUtil.isBlank(mecanico.getNome())) {
            throw new RegraNegocioException("O nome do mecânico é obrigatório.");
        }
        if (mecanico.getNome().length() < 3) {
            throw new RegraNegocioException("O nome do mecânico deve ter no mínimo 3 letras.");
        }
        if (StringsUtil.somenteNumeros(mecanico.getCpf()).length() != 11) {
            throw new RegraNegocioException("Informe um CPF com 11 dígitos.");
        }
        mecanico.setCpf(StringsUtil.formatarCpf(mecanico.getCpf()));
        var existente = repository.findByCpf(mecanico.getCpf());
        if (existente.isPresent() && !existente.get().getId().equals(mecanico.getId())) {
            throw new RegraNegocioException("Já existe um mecânico com este CPF cadastrado.");
        }
        return repository.save(mecanico);
    }

    @Transactional
    public void excluir(Long id) {
        obterPeloId(id);
        if (ordemServicoRepository.existsByMecanicoId(id)) {
            throw new RegraNegocioException("O mecânico possui ordens de serviço e não pode ser excluído.");
        }
        repository.deleteById(id);
    }

}
