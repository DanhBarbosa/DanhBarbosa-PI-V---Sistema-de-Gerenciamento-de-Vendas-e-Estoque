package br.senac.tads.pi.PI_IV.venda.dto;

import jakarta.validation.constraints.NotNull;

public class ItemVendaDto {

    @NotNull
    private Long idProduto;

    @NotNull
    private Integer quantidade;

    @NotNull
    private Double preco;

    public ItemVendaDto() {
    }

    public ItemVendaDto(Long idProduto, Integer quantidade, Double preco) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
