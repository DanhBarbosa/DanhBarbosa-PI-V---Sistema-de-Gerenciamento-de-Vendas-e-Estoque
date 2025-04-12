package br.senac.tads.pi.PI_IV.usuarios;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }


    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }


    @Transactional
    public Usuario atualizarUsuario(Long id, String nome, String senha) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setNome(nome);
        usuario.setSenha(passwordEncoder.encode(senha));
        return usuario;
    }


    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
