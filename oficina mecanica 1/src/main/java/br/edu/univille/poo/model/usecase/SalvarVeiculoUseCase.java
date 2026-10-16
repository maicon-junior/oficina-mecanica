package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.VeiculoDAO;
import br.edu.univille.poo.model.entity.Veiculo;
import br.edu.univille.poo.util.StringsUtil;

import java.time.LocalDate;
import java.util.Objects;

public class SalvarVeiculoUseCase {

    private VeiculoDAO dao;

    public SalvarVeiculoUseCase() {
        dao = new VeiculoDAO();
    }

    public Veiculo executar(Veiculo veiculo) {
        if (StringsUtil.isBlank(veiculo.getPlaca())) {
            throw new IllegalArgumentException("A placa do veículo é obrigatória.");
        }
        veiculo.setPlaca(veiculo.getPlaca().toUpperCase().replace("-", "").trim());
        if (veiculo.getPlaca().length() != 7) {
            throw new IllegalArgumentException("A placa deve ter 7 caracteres (ex.: ABC1D23).");
        }
        if (StringsUtil.isBlank(veiculo.getMarca()) || StringsUtil.isBlank(veiculo.getModelo())) {
            throw new IllegalArgumentException("A marca e o modelo do veículo são obrigatórios.");
        }
        int anoMaximo = LocalDate.now().getYear() + 1;
        if (Objects.isNull(veiculo.getAno()) || veiculo.getAno() < 1900 || veiculo.getAno() > anoMaximo) {
            throw new IllegalArgumentException("Informe um ano entre 1900 e " + anoMaximo + ".");
        }
        if (Objects.isNull(veiculo.getCliente())) {
            throw new IllegalArgumentException("Selecione o cliente (dono do veículo).");
        }
        var existente = dao.obterPelaPlaca(veiculo.getPlaca());
        if (existente.isPresent() && !existente.get().getId().equals(veiculo.getId())) {
            throw new IllegalArgumentException("Já existe um veículo com esta placa cadastrado.");
        }
        if (Objects.isNull(veiculo.getId())) {
            dao.inserir(veiculo);
        } else {
            dao.atualizar(veiculo);
        }
        return veiculo;
    }

}
