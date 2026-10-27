package br.edu.univille.poo.oficina.model.service;

import br.edu.univille.poo.oficina.model.dto.Resumo;
import br.edu.univille.poo.oficina.model.entity.ItemServico;
import br.edu.univille.poo.oficina.model.entity.OrdemServico;
import br.edu.univille.poo.oficina.model.entity.Servico;
import br.edu.univille.poo.oficina.model.entity.StatusOrdem;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.repository.ClienteRepository;
import br.edu.univille.poo.oficina.model.repository.MecanicoRepository;
import br.edu.univille.poo.oficina.model.repository.OrdemServicoRepository;
import br.edu.univille.poo.oficina.model.repository.ServicoRepository;
import br.edu.univille.poo.oficina.model.repository.VeiculoRepository;
import br.edu.univille.poo.oficina.util.StringsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OrdemServicoService {

    private OrdemServicoRepository repository;
    private VeiculoRepository veiculoRepository;
    private MecanicoRepository mecanicoRepository;
    private ServicoRepository servicoRepository;
    private ClienteRepository clienteRepository;

    public OrdemServicoService(OrdemServicoRepository repository, VeiculoRepository veiculoRepository,
                               MecanicoRepository mecanicoRepository, ServicoRepository servicoRepository,
                               ClienteRepository clienteRepository) {
        this.repository = repository;
        this.veiculoRepository = veiculoRepository;
        this.mecanicoRepository = mecanicoRepository;
        this.servicoRepository = servicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<OrdemServico> pesquisar(StatusOrdem status, String placa) {
        return repository.pesquisar(status, placa == null ? "" : placa.trim());
    }

    public List<OrdemServico> obterUltimas() {
        return repository.findTop5ByOrderByDataAberturaDescIdDesc();
    }

    public OrdemServico obterPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Ordem de serviço não encontrada."));
    }

    // Nova OS já aberta na data de hoje
    public OrdemServico nova() {
        OrdemServico ordem = new OrdemServico();
        ordem.setDataAbertura(LocalDate.now());
        ordem.setStatus(StatusOrdem.ABERTA);
        return ordem;
    }

    public Resumo obterResumo() {
        Resumo resumo = new Resumo();
        resumo.setClientes(clienteRepository.count());
        resumo.setVeiculos(veiculoRepository.count());
        resumo.setOrdensAbertas(repository.countByStatus(StatusOrdem.ABERTA));
        resumo.setOrdensEmAndamento(repository.countByStatus(StatusOrdem.EM_ANDAMENTO));
        resumo.setOrdensConcluidas(repository.countByStatus(StatusOrdem.CONCLUIDA));
        return resumo;
    }

    // A OS e os seus itens são gravados na mesma transação: ou tudo é salvo, ou nada é salvo.
    @Transactional
    public OrdemServico salvar(OrdemServico dados) {
        if (Objects.isNull(dados.getVeiculo()) || Objects.isNull(dados.getVeiculo().getId())) {
            throw new RegraNegocioException("Selecione o veículo.");
        }
        if (Objects.isNull(dados.getMecanico()) || Objects.isNull(dados.getMecanico().getId())) {
            throw new RegraNegocioException("Selecione o mecânico responsável.");
        }
        if (Objects.isNull(dados.getStatus())) {
            throw new RegraNegocioException("Selecione o status da ordem.");
        }
        if (Objects.isNull(dados.getDataAbertura())) {
            throw new RegraNegocioException("Informe a data de abertura.");
        }

        // Linhas da tela sem serviço selecionado são ignoradas
        List<ItemServico> itensInformados = new ArrayList<>();
        for (ItemServico item : dados.getItens()) {
            if (item != null && item.getServico() != null && item.getServico().getId() != null) {
                itensInformados.add(item);
            }
        }
        if (itensInformados.isEmpty()) {
            throw new RegraNegocioException("Adicione pelo menos um serviço à ordem.");
        }
        for (ItemServico item : itensInformados) {
            if (Objects.isNull(item.getQuantidade()) || item.getQuantidade() <= 0) {
                throw new RegraNegocioException("A quantidade de cada serviço deve ser maior que zero.");
            }
        }
        if (dados.getStatus() == StatusOrdem.CONCLUIDA && Objects.isNull(dados.getDataConclusao())) {
            dados.setDataConclusao(LocalDate.now());
        }
        if (Objects.nonNull(dados.getDataConclusao()) && dados.getDataConclusao().isBefore(dados.getDataAbertura())) {
            throw new RegraNegocioException("A data de conclusão não pode ser anterior à data de abertura.");
        }

        OrdemServico ordem = Objects.isNull(dados.getId()) ? new OrdemServico() : obterPeloId(dados.getId());

        // Mantém o preço já cobrado dos serviços que continuam na ordem
        Map<Long, BigDecimal> precosAnteriores = new HashMap<>();
        for (ItemServico item : ordem.getItens()) {
            precosAnteriores.put(item.getServico().getId(), item.getValorUnitario());
        }

        ordem.setVeiculo(veiculoRepository.findById(dados.getVeiculo().getId())
                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado.")));
        ordem.setMecanico(mecanicoRepository.findById(dados.getMecanico().getId())
                .orElseThrow(() -> new RegraNegocioException("Mecânico não encontrado.")));
        ordem.setStatus(dados.getStatus());
        ordem.setDataAbertura(dados.getDataAbertura());
        ordem.setDataConclusao(dados.getDataConclusao());
        ordem.setObservacao(StringsUtil.limpar(dados.getObservacao()));

        ordem.getItens().clear();
        for (ItemServico informado : itensInformados) {
            Servico servico = servicoRepository.findById(informado.getServico().getId())
                    .orElseThrow(() -> new RegraNegocioException("Serviço não encontrado."));
            ItemServico item = new ItemServico(servico, informado.getQuantidade());
            item.setValorUnitario(precosAnteriores.getOrDefault(servico.getId(), servico.getValor()));
            ordem.adicionarItem(item);
        }
        return repository.save(ordem);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obterPeloId(id));
    }

}
