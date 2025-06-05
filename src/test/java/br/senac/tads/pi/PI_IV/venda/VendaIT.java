package br.senac.tads.pi.PI_IV.venda;

import br.senac.tads.pi.PI_IV.JwtAuthentication;
import br.senac.tads.pi.PI_IV.venda.dto.ItemVendaDto;
import br.senac.tads.pi.PI_IV.venda.dto.VendaRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VendaIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void salvarVenda_Retorno200() {
        webTestClient
                .post()
                .uri("/api/vendas")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VendaRequestDto("cartão_credito",
                        Arrays.asList(new ItemVendaDto(100L, 1, 20.0))
                ))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void salvarVenda_Retorno400() {
        webTestClient
                .post()
                .uri("/api/vendas")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new VendaRequestDto("cartão_credito",
                        Arrays.asList(new ItemVendaDto(100L, 1000, 20.0))
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(body -> Assertions.assertThat(body).contains("A quantidade solicitada do produto Produto 100 é  maior que se tem no estoque"));

    }

}
