package br.senac.tads.pi.PI_IV.usuario.repository;

import br.senac.tads.pi.PI_IV.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    Usuario findByLogin(String login);
}
