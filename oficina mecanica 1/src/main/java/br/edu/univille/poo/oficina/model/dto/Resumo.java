package br.edu.univille.poo.oficina.model.dto;

// Totais exibidos no painel principal
public class Resumo {
    private long clientes;
    private long veiculos;
    private long ordensAbertas;
    private long ordensEmAndamento;
    private long ordensConcluidas;

    public long getClientes() {
        return clientes;
    }

    public void setClientes(long clientes) {
        this.clientes = clientes;
    }

    public long getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(long veiculos) {
        this.veiculos = veiculos;
    }

    public long getOrdensAbertas() {
        return ordensAbertas;
    }

    public void setOrdensAbertas(long ordensAbertas) {
        this.ordensAbertas = ordensAbertas;
    }

    public long getOrdensEmAndamento() {
        return ordensEmAndamento;
    }

    public void setOrdensEmAndamento(long ordensEmAndamento) {
        this.ordensEmAndamento = ordensEmAndamento;
    }

    public long getOrdensConcluidas() {
        return ordensConcluidas;
    }

    public void setOrdensConcluidas(long ordensConcluidas) {
        this.ordensConcluidas = ordensConcluidas;
    }
}
