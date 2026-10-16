package br.edu.univille.poo.model.usecase;

import br.edu.univille.poo.model.dao.ClienteDAO;
import br.edu.univille.poo.model.dao.OrdemServicoDAO;
import br.edu.univille.poo.model.dao.VeiculoDAO;
import br.edu.univille.poo.model.entity.Resumo;
import br.edu.univille.poo.model.entity.StatusOrdem;

public class ObterResumoUseCase {

    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;
    private OrdemServicoDAO ordemDAO;

    public ObterResumoUseCase() {
        clienteDAO = new ClienteDAO();
        veiculoDAO = new VeiculoDAO();
        ordemDAO = new OrdemServicoDAO();
    }

    public Resumo executar() {
        Resumo resumo = new Resumo();
        resumo.setClientes(clienteDAO.contar());
        resumo.setVeiculos(veiculoDAO.contar());
        resumo.setOrdensAbertas(ordemDAO.contarPorStatus(StatusOrdem.ABERTA));
        resumo.setOrdensEmAndamento(ordemDAO.contarPorStatus(StatusOrdem.EM_ANDAMENTO));
        resumo.setOrdensConcluidas(ordemDAO.contarPorStatus(StatusOrdem.CONCLUIDA));
        return resumo;
    }

}
