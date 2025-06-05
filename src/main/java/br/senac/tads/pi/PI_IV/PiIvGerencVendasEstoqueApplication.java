package br.senac.tads.pi.PI_IV;

import br.senac.tads.pi.PI_IV.usuario.model.Usuario;
import br.senac.tads.pi.PI_IV.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class PiIvGerencVendasEstoqueApplication implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(PiIvGerencVendasEstoqueApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        usuarioRepository.deleteAll();


        if (usuarioRepository.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setLogin("admin@gmail.com");
            usuario.setSenha(passwordEncoder.encode("123456"));
            usuario.setNome("Admin");

            usuarioRepository.save(usuario);
        }


    }
}
