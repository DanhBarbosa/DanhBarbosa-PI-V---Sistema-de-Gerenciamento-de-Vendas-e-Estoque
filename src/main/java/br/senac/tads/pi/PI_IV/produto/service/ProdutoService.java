package br.senac.tads.pi.PI_IV.produto.service;

import br.senac.tads.pi.PI_IV.produto.exception.ProdutoNotFoundException;
import br.senac.tads.pi.PI_IV.produto.model.Produto;
import br.senac.tads.pi.PI_IV.produto.repository.ProdutoRepository;
import br.senac.tads.pi.PI_IV.venda.exception.ErroNaVendaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto editarProduto(Long id, Produto produto) {
        Optional<Produto> produtoOpt = buscarProdutoPorId(id);
        if (!produtoOpt.isPresent()) {
            throw new ProdutoNotFoundException(id);
        }

        Produto produtoExistente = produtoOpt.get();
        produtoExistente.setCodigo(produto.getCodigo());
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setMarca(produto.getMarca());
        produtoExistente.setQuantidade(produto.getQuantidade());
        produtoExistente.setPreco(produto.getPreco());

        return produto;
    }

    public void excluirProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }


    @Transactional
    public void atualizarQuantidadeDoProduto(Long id, Integer quantidade) {
        Produto produto = buscarProdutoPorId(id).get();
        Integer qtdAtual = produto.getQuantidade();
        produto.setQuantidade(qtdAtual - quantidade);

    }

    public void validarQuantidadeProduto(Long id, Integer quantidade) {

        Produto produto = buscarProdutoPorId(id).get();
        Integer qtdAtual = produto.getQuantidade();

        if (quantidade > qtdAtual) {
            throw new ErroNaVendaException(String.format("A quantidade solicitada do produto %s é  maior que se tem no estoque", produto.getNome()));
        }
    }


}
