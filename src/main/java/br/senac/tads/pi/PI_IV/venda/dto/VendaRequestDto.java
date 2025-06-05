package br.senac.tads.pi.PI_IV.venda.dto;

import java.util.List;

public class VendaRequestDto {

    private String metodoPagamento;

    private List<ItemVendaDto> itens;

    public VendaRequestDto() {
    }

    public VendaRequestDto(String metodoPagamento, List<ItemVendaDto> itens) {
        this.metodoPagamento = metodoPagamento;
        this.itens = itens;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public List<ItemVendaDto> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaDto> itens) {
        this.itens = itens;
    }
}
