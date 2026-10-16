package br.edu.univille.poo.model.exception;

public class MecanicoNotFoundException extends RuntimeException {

    public MecanicoNotFoundException() {
        super("O mecânico não foi encontrado.");
    }

}
