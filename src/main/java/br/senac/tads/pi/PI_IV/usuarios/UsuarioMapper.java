package br.senac.tads.pi.PI_IV.usuarios;

import java.util.List;

public class UsuarioMapper {


    public static Usuario toUsuario(UsuarioRequestoDTO usuarioRequestoDTO) {
        return new Usuario(usuarioRequestoDTO.nome(), usuarioRequestoDTO.login(), usuarioRequestoDTO.senha());
    }

    public static UsuarioListResponseDTO toUsuarioResponseDTO(Usuario usuario) {
        return new UsuarioListResponseDTO(usuario.getId(), usuario.getNome(), usuario.getLogin(), usuario.getSenha());
    }


    public static List<UsuarioListResponseDTO> toUsuarioListResponseDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(usuario -> toUsuarioResponseDTO(usuario)).toList();

    }


}
