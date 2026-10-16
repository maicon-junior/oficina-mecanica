package br.edu.univille.poo.model.exception;

public class MecanicoComOrdensException extends RuntimeException {

    public MecanicoComOrdensException() {
        super("O mecânico possui ordens de serviço e não pode ser excluído.");
    }

}
