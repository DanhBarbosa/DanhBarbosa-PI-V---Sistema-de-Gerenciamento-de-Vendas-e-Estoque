package br.senac.tads.pi.PI_IV.usuario;

import br.senac.tads.pi.PI_IV.JwtAuthentication;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioListResponseDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioRequestoDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioUpdateRequestoDTO;
import br.senac.tads.pi.PI_IV.usuario.model.Usuario;
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
public class UsuariosIT {

    @Autowired
    WebTestClient webTestClient;

    // Salvar Usuario Válido
    @Test
    public void salvarUsuarioValido_Retorno201() {
        webTestClient
                .post()
                .uri("api/usuarios")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestoDTO("João Marques", "joao@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(201);
    }


    // Salvar Usuario Inválido
    @Test
    public void salvarUsuarioInvalido_Retorno400() {
        webTestClient
                .post()
                .uri("api/usuarios")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestoDTO("João Marques", "joao@gmail.com", null))
                .exchange()
                .expectStatus().isEqualTo(400);
    }


    // Buscar Usuario Por ID Válido
    @Test
    public void buscarUsuarioValido_Retorno200() {
        Usuario responseBody = webTestClient
                .get()
                .uri("api/usuarios/101")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(Usuario.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody.getId()).isNotNull();

    }

    // Buscar Usuario Por ID Inválido
    @Test
    public void buscarUsuarioInvalido_Retorno400() {
        webTestClient
                .get()
                .uri("api/usuarios/300")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(404);
    }

    // Editar Usuario Válido
    @Test
    void editarUsuarioValido_Retorno200() {
        UsuarioListResponseDTO responseBody = webTestClient
                .patch()
                .uri("api/usuarios/101")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioUpdateRequestoDTO("Admin Master", "1234567"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioListResponseDTO.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.nome()).isEqualTo("Admin Master");
    }

    // Editar Usuario Inválido
    @Test
    void editarUsuarioInvalido_Retorno404() {
        webTestClient
                .put()
                .uri("api/usuarios/1000")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioUpdateRequestoDTO("Admin Master", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(403);

    }

    // Excluir Usuario
    @Test
    void deletarUsuario_Retorno204() {
        webTestClient
                .delete()
                .uri("api/usuarios/1")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "admin@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(200);
    }
}
