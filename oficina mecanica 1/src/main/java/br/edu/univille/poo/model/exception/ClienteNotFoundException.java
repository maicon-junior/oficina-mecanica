package br.edu.univille.poo.model.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException() {
        super("O cliente não foi encontrado.");
    }

}
