package br.senac.tads.pi.PI_IV.usuario.mapper;

import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioListResponseDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioRequestoDTO;
import br.senac.tads.pi.PI_IV.usuario.dto.UsuarioResponseDTO;
import br.senac.tads.pi.PI_IV.usuario.model.Usuario;

import java.util.List;

public class UsuarioMapper {


    public static Usuario toUsuario(UsuarioRequestoDTO usuarioRequestoDTO) {
        return new Usuario(usuarioRequestoDTO.nome(), usuarioRequestoDTO.login(), usuarioRequestoDTO.senha());
    }

    public static UsuarioListResponseDTO toUsuarioListResponseDTO(Usuario usuario) {
        return new UsuarioListResponseDTO(usuario.getId(), usuario.getNome(), usuario.getLogin(), usuario.getSenha());
    }

    private static UsuarioResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getNome());
    }


    public static List<UsuarioListResponseDTO> toUsuarioListResponseDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(usuario -> toUsuarioListResponseDTO(usuario)).toList();

    }


    public static List<UsuarioResponseDTO> toUsuarioResponseDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(usuario -> toUsuarioResponseDTO(usuario)).toList();

    }


}
