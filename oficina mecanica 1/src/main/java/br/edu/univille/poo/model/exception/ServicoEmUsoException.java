package br.edu.univille.poo.model.exception;

public class ServicoEmUsoException extends RuntimeException {

    public ServicoEmUsoException() {
        super("O serviço já foi usado em ordens de serviço e não pode ser excluído.");
    }

}
