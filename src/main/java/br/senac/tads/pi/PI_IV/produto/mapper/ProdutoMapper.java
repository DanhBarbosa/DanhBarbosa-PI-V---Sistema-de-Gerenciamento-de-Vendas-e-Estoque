package br.senac.tads.pi.PI_IV.produto.mapper;

import br.senac.tads.pi.PI_IV.produto.dto.ProdutoResponseDTO;
import br.senac.tads.pi.PI_IV.produto.model.Produto;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoMapper {


    private static ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(produto.getNome());
    }


    public static List<ProdutoResponseDTO> toProdutoResponse(List<Produto> produtos) {
        return produtos.stream().map(produto -> toResponseDTO(produto)).collect(Collectors.toList());
    }
}
