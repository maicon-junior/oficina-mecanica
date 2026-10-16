package br.edu.univille.poo.control.mecanico;

import br.edu.univille.poo.model.entity.Mecanico;
import br.edu.univille.poo.model.usecase.ExcluirMecanicoUseCase;
import br.edu.univille.poo.model.usecase.PesquisarMecanicosUseCase;
import br.edu.univille.poo.model.usecase.SalvarMecanicoUseCase;

import java.util.List;

public class MecanicoController {

    private PesquisarMecanicosUseCase pesquisarUseCase;
    private SalvarMecanicoUseCase salvarUseCase;
    private ExcluirMecanicoUseCase excluirUseCase;

    public MecanicoController() {
        pesquisarUseCase = new PesquisarMecanicosUseCase();
        salvarUseCase = new SalvarMecanicoUseCase();
        excluirUseCase = new ExcluirMecanicoUseCase();
    }

    public List<Mecanico> pesquisar(String nome) {
        return pesquisarUseCase.executar(nome);
    }

    public void salvar(Mecanico mecanico) {
        salvarUseCase.executar(mecanico);
    }

    public void excluir(Integer id) {
        excluirUseCase.executar(id);
    }

}
