package br.edu.univille.poo.model.exception;

public class OrdemServicoNotFoundException extends RuntimeException {

    public OrdemServicoNotFoundException() {
        super("A ordem de serviço não foi encontrada.");
    }

}
