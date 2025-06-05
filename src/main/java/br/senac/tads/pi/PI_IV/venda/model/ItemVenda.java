package br.senac.tads.pi.PI_IV.venda.model;


import jakarta.persistence.*;

@Entity
@Table
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venda_id")
    private Venda venda;

    private Long idProduto;

    private Integer quantidade;

    private Double preco;

    public ItemVenda() {
    }

    public ItemVenda(Venda venda, Long idProduto, Integer quantidade, Double preco) {
        this.venda = venda;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
