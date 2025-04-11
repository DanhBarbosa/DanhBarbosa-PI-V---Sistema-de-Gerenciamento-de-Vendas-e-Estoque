package br.senac.tads.pi.PI_IV;

import br.senac.tads.pi.PI_IV.auth.UsuarioAuthRequestDTO;
import br.senac.tads.pi.PI_IV.security.jwt.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureMockMvc
public class AuthIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void autenticarComCredenciasValidas_Retorno200() {

        JwtToken responseBody = webTestClient.post()
                .uri("auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioAuthRequestDTO("admin@gmail.com", "1234567"))
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
                .bodyValue(new UsuarioAuthRequestDTO("admin@gmail.com", "1234567"))
                .exchange()
                .expectStatus().isForbidden();




    }


}
