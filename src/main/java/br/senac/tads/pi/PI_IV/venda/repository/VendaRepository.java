package br.senac.tads.pi.PI_IV.venda.repository;

import br.senac.tads.pi.PI_IV.venda.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {


    @Query("SELECT v FROM Venda v " +
            "WHERE (:nomeVendedor IS NULL OR v.nomeVendedor = :nomeVendedor) " +
            "AND (:inicioDoDia IS NULL OR v.dataVenda >= :inicioDoDia) " +
            "AND (:fimDoDia IS NULL OR v.dataVenda <= :fimDoDia)")
    List<Venda> buscarVendas(
            @Param("nomeVendedor") String nomeVendedor,
            @Param("inicioDoDia") LocalDateTime inicioDoDia,
            @Param("fimDoDia") LocalDateTime fimDoDia);

}
