package br.edu.univille.poo.oficina;

import br.edu.univille.poo.oficina.model.entity.Cliente;
import br.edu.univille.poo.oficina.model.entity.ItemServico;
import br.edu.univille.poo.oficina.model.entity.OrdemServico;
import br.edu.univille.poo.oficina.model.entity.StatusOrdem;
import br.edu.univille.poo.oficina.model.exception.RegraNegocioException;
import br.edu.univille.poo.oficina.model.service.ClienteService;
import br.edu.univille.poo.oficina.model.service.MecanicoService;
import br.edu.univille.poo.oficina.model.service.OrdemServicoService;
import br.edu.univille.poo.oficina.model.service.ServicoService;
import br.edu.univille.poo.oficina.model.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OficinaApplicationTests {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private MecanicoService mecanicoService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private OrdemServicoService ordemServicoService;

    @Test
    void carregaDadosIniciais() {
        assertEquals(3, clienteService.obterTodos().size());
        assertEquals(4, veiculoService.obterTodos().size());
        assertEquals(1, ordemServicoService.obterResumo().getOrdensAbertas());
    }

    @Test
    void salvaClienteComCpfFormatado() {
        Cliente cliente = clienteService.salvar(new Cliente("Ana Teste", "55566677788", null, null, null));
        assertEquals("555.666.777-88", cliente.getCpf());
    }

    @Test
    void naoPermiteCpfRepetido() {
        assertThrows(RegraNegocioException.class,
                () -> clienteService.salvar(new Cliente("Outro", "123.456.789-00", null, null, null)));
    }

    @Test
    void naoExcluiClienteComVeiculos() {
        assertThrows(RegraNegocioException.class, () -> clienteService.excluir(1L));
    }

    @Test
    void naoExcluiServicoUsadoEmOrdem() {
        assertThrows(RegraNegocioException.class, () -> servicoService.excluir(1L));
    }

    @Test
    void salvaOrdemCalculandoTotal() {
        OrdemServico ordem = ordemServicoService.nova();
        ordem.setVeiculo(veiculoService.obterPeloId(2L));
        ordem.setMecanico(mecanicoService.obterPeloId(1L));
        ordem.getItens().add(new ItemServico(servicoService.obterPeloId(1L), 1));
        ordem.getItens().add(new ItemServico(servicoService.obterPeloId(4L), 2));

        OrdemServico salva = ordemServicoService.salvar(ordem);

        assertEquals(0, new BigDecimal("820.00").compareTo(salva.getValorTotal()));
        assertEquals(StatusOrdem.ABERTA, salva.getStatus());
    }

    @Test
    void naoSalvaOrdemSemServicos() {
        OrdemServico ordem = ordemServicoService.nova();
        ordem.setVeiculo(veiculoService.obterPeloId(2L));
        ordem.setMecanico(mecanicoService.obterPeloId(1L));
        assertThrows(RegraNegocioException.class, () -> ordemServicoService.salvar(ordem));
    }

}
