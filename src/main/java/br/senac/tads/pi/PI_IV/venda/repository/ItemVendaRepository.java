package br.senac.tads.pi.PI_IV.venda.repository;

import br.senac.tads.pi.PI_IV.venda.model.ItemVenda;
import br.senac.tads.pi.PI_IV.venda.repository.projection.VendaProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    @Query("select i.venda.id AS idVenda, i.venda.nomeVendedor AS nomeFuncionario, i.venda.dataVenda AS dataVenda, i.quantidade AS quantidade, i.venda.totalVenda AS valorVenda, i.venda.metodoPagamento as metodoPagamento, i.idProduto as idProduto from ItemVenda i")
    List<VendaProjection> findAllVendas();


}
