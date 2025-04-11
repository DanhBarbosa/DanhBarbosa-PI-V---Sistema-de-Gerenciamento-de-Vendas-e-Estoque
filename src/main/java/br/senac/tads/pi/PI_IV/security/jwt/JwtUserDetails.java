package br.senac.tads.pi.PI_IV.security.jwt;

import br.senac.tads.pi.PI_IV.usuarios.Usuario;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getLogin(), usuario.getSenha(), AuthorityUtils.createAuthorityList(new String[] { "ROLE_ADMIN" }));
        this.usuario = usuario;
    }

    public Long getId() {
        return this.usuario.getId();
    }

//    public String getRole() {
//        return this.usuario.getRole().name();
//    }
}
