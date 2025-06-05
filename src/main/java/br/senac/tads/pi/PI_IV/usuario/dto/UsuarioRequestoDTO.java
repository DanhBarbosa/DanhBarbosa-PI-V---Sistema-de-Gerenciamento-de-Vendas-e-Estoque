package br.senac.tads.pi.PI_IV.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestoDTO(@NotNull String nome, @NotNull @Email String login, @NotNull String senha) {


}
