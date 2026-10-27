package br.edu.univille.poo.oficina.model.exception;

// Lançada pelos Services quando uma regra de negócio é violada.
// O Controller captura e mostra a mensagem na tela.
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

}
