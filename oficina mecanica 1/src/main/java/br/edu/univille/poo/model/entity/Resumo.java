package br.edu.univille.poo.model.entity;

public class Resumo {
    private int clientes;
    private int veiculos;
    private int ordensAbertas;
    private int ordensEmAndamento;
    private int ordensConcluidas;

    public int getClientes() {
        return clientes;
    }

    public void setClientes(int clientes) {
        this.clientes = clientes;
    }

    public int getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(int veiculos) {
        this.veiculos = veiculos;
    }

    public int getOrdensAbertas() {
        return ordensAbertas;
    }

    public void setOrdensAbertas(int ordensAbertas) {
        this.ordensAbertas = ordensAbertas;
    }

    public int getOrdensEmAndamento() {
        return ordensEmAndamento;
    }

    public void setOrdensEmAndamento(int ordensEmAndamento) {
        this.ordensEmAndamento = ordensEmAndamento;
    }

    public int getOrdensConcluidas() {
        return ordensConcluidas;
    }

    public void setOrdensConcluidas(int ordensConcluidas) {
        this.ordensConcluidas = ordensConcluidas;
    }

}
