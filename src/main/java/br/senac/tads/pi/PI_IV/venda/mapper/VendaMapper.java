package br.senac.tads.pi.PI_IV.venda.mapper;

import br.senac.tads.pi.PI_IV.venda.dto.ItemVendaDto;
import br.senac.tads.pi.PI_IV.venda.dto.VendaRequestDto;
import br.senac.tads.pi.PI_IV.venda.model.ItemVenda;
import br.senac.tads.pi.PI_IV.venda.model.Venda;

import java.util.ArrayList;
import java.util.List;

public class VendaMapper {



    public static Venda toModel(VendaRequestDto vendaRequestDto) {
        Venda venda = new Venda();

        vendaRequestDto.getItens().forEach(item -> {

            double total = item.getPreco() * item.getQuantidade();
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setIdProduto(item.getIdProduto());
            itemVenda.setPreco(item.getPreco());
            itemVenda.setQuantidade(item.getQuantidade());
            venda.getItens().add(itemVenda);
            venda.setTotalVenda(total);

        });
        venda.setMetodoPagamento(vendaRequestDto.getMetodoPagamento());
        return venda;
    }


    public static List<ItemVenda> toListItemVenda(List<ItemVendaDto> itemVendaDto) {
        List<ItemVenda> itensVendas = new ArrayList<>();

        itemVendaDto.forEach(item -> {
            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setIdProduto(item.getIdProduto());
            itemVenda.setPreco(item.getPreco());
            itemVenda.setQuantidade(item.getQuantidade());
            itensVendas.add(itemVenda);
        });

        return itensVendas;
    }
}
