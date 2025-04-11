package br.senac.tads.pi.PI_IV;

import br.senac.tads.pi.PI_IV.auth.UsuarioAuthRequestDTO;
import br.senac.tads.pi.PI_IV.security.jwt.JwtToken;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {
    public static Consumer<HttpHeaders> getHeadersAuthorization(WebTestClient client, String email, String password) {

        String token = client.
                post()
                .uri("/auth/login")
                .bodyValue(new UsuarioAuthRequestDTO(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
