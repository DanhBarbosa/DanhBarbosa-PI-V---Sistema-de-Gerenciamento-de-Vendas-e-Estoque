package br.senac.tads.pi.PI_IV.produtos;

import br.senac.tads.pi.PI_IV.produto.Produto;
import br.senac.tads.pi.PI_IV.produto.ProdutoNotFoundException;
import br.senac.tads.pi.PI_IV.produto.ProdutoRepository;
import br.senac.tads.pi.PI_IV.produto.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProdutoTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Criar Produto

    @Test
    void testValidoCriarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto 1");
        produto.setCodigo("1234567890");
        produto.setDescricao("Descrição Produto 1");
        produto.setMarca("Marca Produto 1");
        produto.setQuantidade(10);
        when(repository.save(produto)).thenReturn(produto);
        Produto produtoSalvo = service.salvarProduto(produto);
        when(repository.findById(produtoSalvo.getId())).thenReturn(Optional.of(produtoSalvo));
        System.out.println(produtoSalvo.getId());
        assertThat(produtoSalvo.getId()).isNotNull();
        assertThat(produtoSalvo.getNome()).isEqualTo("Produto 1");
        assertThat(repository.findById(produtoSalvo.getId())).isPresent();
    }

    @Test
    void testInvalidoCriarProduto() {
        Produto produto = new Produto();
        produto.setId(2L);
        produto.setNome(null);
        produto.setCodigo(null);
        produto.setDescricao("Descrição Produto 1");
        produto.setMarca("Marca Produto 1");
        produto.setQuantidade(10);
        Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("codigo")));
        verify(repository, times(0)).save(any(Produto.class));
    }

    // Buscar Produto por ID
    @Test
    void testValidBuscarProduto() {
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto 1");
        when(repository.findById(id)).thenReturn(Optional.of(produto));
        Optional<Produto> result = service.buscarProdutoPorId(id);
        assertTrue(result.isPresent());
        assertEquals("Produto 1", result.get().getNome());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testInvalidBuscarProduto() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Produto> result = service.buscarProdutoPorId(id);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findById(id);
    }

    // Editar Produto por ID
    @Test
    void testValidEditarProduto() {
        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Nome Antigo Produto 1");
        produto.setQuantidade(10);
        produto.setPreco(20.0);
        Produto produtoAtualizado = new Produto();
        produto.setId(produtoId);
        produtoAtualizado.setNome("Nome Novo Produto 1");
        produtoAtualizado.setQuantidade(20);
        produtoAtualizado.setPreco(30.0);
        when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(repository.save(any(Produto.class))).thenReturn(produtoAtualizado);
        Produto result = service.editarProduto(produtoId, produtoAtualizado);
        assertEquals("Nome Novo Produto 1", result.getNome());
        assertEquals(20, result.getQuantidade());
        assertEquals(30.0, result.getPreco());
        verify(repository, times(1)).findById(produtoId);
        verify(repository, times(1)).save(any(Produto.class));
    }

    @Test
    void testInvalidEditarProduto() {
        Long produtoId = 1L;
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(produtoId);
        produtoAtualizado.setNome("Nome Novo Produto 1");
        produtoAtualizado.setQuantidade(20);
        produtoAtualizado.setPreco(30.0);
        when(repository.findById(produtoId)).thenReturn(Optional.empty());
        ProdutoNotFoundException exception = assertThrows(ProdutoNotFoundException.class, () -> {
            service.editarProduto(produtoId, produtoAtualizado);
        });
        assertEquals("Produto não encontrado com ID: " + produtoId, exception.getMessage());
        verify(repository, times(1)).findById(produtoId);
        verify(repository, never()).save(any(Produto.class));
    }

    // Excluir Produto por ID

    @Test
    void testValidDeletarProduto() {
        Long idProduto = 1L;
        service.excluirProduto(idProduto);
        verify(repository, times(1)).deleteById(idProduto);
    }

}
