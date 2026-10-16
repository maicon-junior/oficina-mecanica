package br.edu.univille.poo.model.exception;

public class ClienteComVeiculosException extends RuntimeException {

    public ClienteComVeiculosException() {
        super("O cliente possui veículos cadastrados. Exclua os veículos primeiro.");
    }

}
