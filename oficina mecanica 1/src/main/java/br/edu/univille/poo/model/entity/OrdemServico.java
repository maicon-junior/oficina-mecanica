package br.edu.univille.poo.model.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private Integer id;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    private StatusOrdem status;
    private String observacao;
    private Veiculo veiculo;
    private Mecanico mecanico;
    private List<ItemServico> itens = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StatusOrdem getStatus() {
        return status;
    }

    public void setStatus(StatusOrdem status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Mecanico getMecanico() {
        return mecanico;
    }

    public void setMecanico(Mecanico mecanico) {
        this.mecanico = mecanico;
    }

    public List<ItemServico> getItens() {
        return itens;
    }

    public void setItens(List<ItemServico> itens) {
        this.itens = itens;
    }

    public void adicionarItem(ItemServico item) {
        itens.add(item);
    }

    public void removerItem(int indice) {
        itens.remove(indice);
    }

    public double getValorTotal() {
        double total = 0;
        for (ItemServico item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

}
