package br.senac.tads.pi.PI_IV.venda.repository.projection;

import java.time.LocalDateTime;

public interface VendaProjection {

    Long getIdVenda();

    String getNomeFuncionario();
                                                                                                                                                                                                        
    LocalDateTime getDataVenda();

    Integer getQuantidade();

    Double getValorVenda();

    String getMetodoPagamento();

    Long getIdProduto();
}
