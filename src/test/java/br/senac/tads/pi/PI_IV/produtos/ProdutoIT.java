package br.senac.tads.pi.PI_IV.produtos;

import br.senac.tads.pi.PI_IV.JwtAuthentication;
import br.senac.tads.pi.PI_IV.produto.model.Produto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProdutoIT {

    @Autowired
    WebTestClient webTestClient;

    // Salvar Produto Válido
    @Test
    public void salvarProdutoValido_Retorno201() {
        webTestClient
                .post()
                .uri("api/produtos")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Produto("01234567890", "Produto 1", "Descrição Produto 1",
                        "Marca Produto 1", 12, 20.0))
                .exchange()
                .expectStatus().isEqualTo(201);
    }


    // Salvar Produto Inválido

    @Test
    public void salvarProdutoInvalido_Retorno400() {
        webTestClient
                .post()
                .uri("api/produtos")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Produto("01234567890", null, "Descrição Produto 1",
                        "Marca Produto 1", 12, 20.0))
                .exchange()
                .expectStatus().isEqualTo(400);
    }

    // Listar Produtos
    @Test
    public void listarProdutos_Retorno200() {

        List<Produto> responseBody = webTestClient
                .get()
                .uri("api/produtos")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Produto.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody.size()).isEqualTo(1);
    }


    // Buscar Produto Por ID Válido
    @Test
    public void buscarProdutoValido_Retorno200() {
        Produto responseBody = webTestClient
                .get()
                .uri("api/produtos/100")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(Produto.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getNome()).isEqualTo("Produto 100");
    }

    // Buscar Produto Por ID Inválido
    @Test
    public void buscarProdutoInvalido_Retorno404() {
        webTestClient
                .get()
                .uri("api/produtos/101")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    // Editar Produto Válido
    @Test
    void editarProdutoValido_Retorno200() {
        Produto responseBody = webTestClient
                .put()
                .uri("api/produtos/100")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Produto("01234567890", "Novo Nome Produto 1", "Descrição Produto 1",
                        "Marca Produto 1", 12, 20.0))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Produto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getNome()).isEqualTo("Novo Nome Produto 1");
    }

    // Editar Produto Inválido
    @Test
    void editarProdutoInvalido_Retorno404() {
       webTestClient
                .put()
                .uri("api/produtos/101")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Produto("01234567890", "Novo Nome Produto 1", "Descrição Produto 1",
                        "Marca Produto 1", 12, 20.0))
                .exchange()
                .expectStatus().isNotFound();
    }

    // Excluir Produto
    @Test
    void deletarProduto_Retorno204() {
        webTestClient
                .delete()
                .uri("api/produtos/1")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(204);
    }
}
