package br.senac.tads.pi.PI_IV.produto;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

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

        return produtoRepository.save(produto);
    }

    public void excluirProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }
}
