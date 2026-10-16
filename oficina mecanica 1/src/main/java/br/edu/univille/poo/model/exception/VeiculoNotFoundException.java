package br.edu.univille.poo.model.exception;

public class VeiculoNotFoundException extends RuntimeException {

    public VeiculoNotFoundException() {
        super("O veículo não foi encontrado.");
    }

}
