package br.senac.tads.pi.PI_IV.usuarios;


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

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioListResponseDTO> buscar(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDTO(usuario));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioListResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioUpdateRequestoDTO usuarioUpdateRequestoDTO) {
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioUpdateRequestoDTO.nome(), usuarioUpdateRequestoDTO.senha());
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDTO(usuario));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok().build();
    }
}
