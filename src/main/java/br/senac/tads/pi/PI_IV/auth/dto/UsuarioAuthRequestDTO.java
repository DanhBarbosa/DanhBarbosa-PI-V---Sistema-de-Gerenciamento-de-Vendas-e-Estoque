package br.senac.tads.pi.PI_IV.auth.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioAuthRequestDTO(@NotNull String login, @NotNull String senha) {
}
