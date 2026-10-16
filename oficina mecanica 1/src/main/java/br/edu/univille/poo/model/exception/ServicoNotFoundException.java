package br.edu.univille.poo.model.exception;

public class ServicoNotFoundException extends RuntimeException {

    public ServicoNotFoundException() {
        super("O serviço não foi encontrado.");
    }

}
