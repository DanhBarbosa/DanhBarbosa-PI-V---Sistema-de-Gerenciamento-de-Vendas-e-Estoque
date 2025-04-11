package br.senac.tads.pi.PI_IV.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    Usuario findByLogin(String login);
}
