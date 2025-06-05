package br.senac.tads.pi.PI_IV.venda.dto;

import java.time.LocalDateTime;

public class VendaResponseDto {

    private Long idVenda;
    private String nomeFuncionario;
    private Integer quantidade;
    private Double valorVenda;
    private LocalDateTime dataVenda;
    private String metodoPagamento;

    private String nomeProduto;


    public VendaResponseDto(Long idVenda, String nomeFuncionario, LocalDateTime dataVenda, Integer quantidade, Double valorVenda, String metodoPagamento, String nomeProduto) {
        this.idVenda = idVenda;
        this.nomeFuncionario = nomeFuncionario;
        this.quantidade = quantidade;
        this.valorVenda = valorVenda;
        this.dataVenda = dataVenda;
        this.metodoPagamento = metodoPagamento;
        this.nomeProduto = nomeProduto;

    }

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorVenda() {
        return valorVenda;
    }


    public LocalDateTime getDataVenda() {
        return dataVenda;
    }


    public String getMetodoPagamento() {
        return metodoPagamento;
    }


    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    @Override
    public String toString() {
        return "VendaResponseDto{" +
                "idVenda=" + idVenda +
                ", nomeFuncionario='" + nomeFuncionario + '\'' +
                ", quantidade=" + quantidade +
                ", valor=" + valorVenda +
                ", dataVenda=" + dataVenda +
                '}';
    }
}
