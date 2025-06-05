package br.senac.tads.pi.PI_IV.usuario.controller;


import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioListResponseDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioRequestoDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioResponseDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioUpdateRequestoDTO;
import br.senac.tads.pi.PI_IV.usuario.mapper.UsuarioMapper;
import br.senac.tads.pi.PI_IV.usuario.model.Usuario;
import br.senac.tads.pi.PI_IV.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping
    public ResponseEntity<Void> salvar(@Valid @RequestBody UsuarioRequestoDTO usuarioRequestoDTO) {
        usuarioService.salvarUsuario(UsuarioMapper.toUsuario((usuarioRequestoDTO)));
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioListResponseDTO>> listar() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(UsuarioMapper.toUsuarioListResponseDTO(usuarios));
    }

    @GetMapping("/funcionarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDTO(usuarios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioListResponseDTO> buscar(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toUsuarioListResponseDTO(usuario));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioListResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioUpdateRequestoDTO usuarioUpdateRequestoDTO) {
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioUpdateRequestoDTO.nome(), usuarioUpdateRequestoDTO.senha());
        return ResponseEntity.ok(UsuarioMapper.toUsuarioListResponseDTO(usuario));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok().build();
    }
}
