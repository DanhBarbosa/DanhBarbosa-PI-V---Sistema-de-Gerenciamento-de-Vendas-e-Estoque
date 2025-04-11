package br.senac.tads.pi.PI_IV.produto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable Long id) {
        Optional<Produto> produtoOpt = produtoService.buscarProdutoPorId(id);

        if (produtoOpt.isPresent()) {
            return ResponseEntity.ok(produtoOpt.get());
        } else {
            throw new ProdutoNotFoundException(id);
        }
    }

    @PostMapping
    public ResponseEntity<?> salvarProduto(@Valid @RequestBody Produto produto) {
        produtoService.salvarProduto(produto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> editarProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Produto produtoAtualizado = produtoService.editarProduto(id, produto);
        return ResponseEntity.ok(produtoAtualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        produtoService.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }
}
