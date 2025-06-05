package br.senac.tads.pi.PI_IV;

import br.senac.tads.pi.PI_IV.auth.dto.UsuarioAuthRequestDTO;
import br.senac.tads.pi.PI_IV.security.jwt.JwtToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void autenticarComCredenciasValidas_Retorno200() {
        JwtToken responseBody = webTestClient.post()
                .uri("auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioAuthRequestDTO("admin@test.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNotNull();
    }
    @Test
    public void autenticarComCredenciasInvalidas_Retorno400() {

        webTestClient.post()
                .uri("auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioAuthRequestDTO("admin@test.com", "1234567"))
                .exchange()
                .expectStatus().isBadRequest();
    }


}
