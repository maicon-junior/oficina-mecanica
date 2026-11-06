package br.edu.univille.poo.oficina.model.service;

import br.edu.univille.poo.oficina.model.entity.Veiculo;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.repository.ClienteRepository;
import br.edu.univille.poo.oficina.model.repository.OrdemServicoRepository;
import br.edu.univille.poo.oficina.model.repository.VeiculoRepository;
import br.edu.univille.poo.oficina.util.StringsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class VeiculoService {

    private VeiculoRepository repository;
    private ClienteRepository clienteRepository;
    private OrdemServicoRepository ordemServicoRepository;

    public VeiculoService(VeiculoRepository repository, ClienteRepository clienteRepository,
                          OrdemServicoRepository ordemServicoRepository) {
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.ordemServicoRepository = ordemServicoRepository;
    }

    public List<Veiculo> obterTodos() {
        return repository.findAllByOrderByPlacaAsc();
    }

    public List<Veiculo> pesquisar(String busca) {
        if (StringsUtil.isBlank(busca)) {
            return obterTodos();
        }
        return repository.pesquisar(busca.trim());
    }

    public Veiculo obterPeloId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado."));
    }

    public long contar() {
        return repository.count();
    }

    @Transactional
    public Veiculo salvar(Veiculo veiculo) {
        if (StringsUtil.isBlank(veiculo.getPlaca())) {
            throw new RegraNegocioException("A placa do veículo é obrigatória.");
        }
        veiculo.setPlaca(veiculo.getPlaca().toUpperCase().replace("-", "").trim());
        if (veiculo.getPlaca().length() != 7) {
            throw new RegraNegocioException("A placa deve ter 7 caracteres (ex.: ABC1D23).");
        }
        if (StringsUtil.isBlank(veiculo.getMarca()) || StringsUtil.isBlank(veiculo.getModelo())) {
            throw new RegraNegocioException("A marca e o modelo do veículo são obrigatórios.");
        }
        int anoMaximo = LocalDate.now().getYear() + 1;
        if (Objects.isNull(veiculo.getAno()) || veiculo.getAno() < 1900 || veiculo.getAno() > anoMaximo) {
            throw new RegraNegocioException("Informe um ano entre 1900 e " + anoMaximo + ".");
        }
        if (Objects.isNull(veiculo.getCliente()) || Objects.isNull(veiculo.getCliente().getId())) {
            throw new RegraNegocioException("Selecione o cliente (dono do veículo).");
        }
        veiculo.setCliente(clienteRepository.findById(veiculo.getCliente().getId())
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado.")));
        var existente = repository.findByPlaca(veiculo.getPlaca());
        if (existente.isPresent() && !existente.get().getId().equals(veiculo.getId())) {
            throw new RegraNegocioException("Já existe um veículo com esta placa cadastrado.");
        }
        return repository.save(veiculo);
    }

    @Transactional
    public void excluir(Long id) {
        obterPeloId(id);
        if (ordemServicoRepository.existsByVeiculoId(id)) {
            throw new RegraNegocioException("O veículo possui ordens de serviço e não pode ser excluído.");
        }
        repository.deleteById(id);
    }

}
