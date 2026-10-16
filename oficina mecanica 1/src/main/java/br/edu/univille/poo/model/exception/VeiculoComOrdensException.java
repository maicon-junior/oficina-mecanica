package br.edu.univille.poo.model.exception;

public class VeiculoComOrdensException extends RuntimeException {

    public VeiculoComOrdensException() {
        super("O veículo possui ordens de serviço e não pode ser excluído.");
    }

}
