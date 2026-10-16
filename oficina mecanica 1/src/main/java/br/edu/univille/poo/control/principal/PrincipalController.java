package br.edu.univille.poo.control.principal;

import br.edu.univille.poo.model.entity.Resumo;
import br.edu.univille.poo.model.usecase.ObterResumoUseCase;

public class PrincipalController {

    private ObterResumoUseCase resumoUseCase;

    public PrincipalController() {
        resumoUseCase = new ObterResumoUseCase();
    }

    public Resumo obterResumo() {
        return resumoUseCase.executar();
    }

}
