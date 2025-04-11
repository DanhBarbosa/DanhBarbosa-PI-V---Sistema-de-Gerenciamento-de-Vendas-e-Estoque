package br.senac.tads.pi.PI_IV.auth;

import br.senac.tads.pi.PI_IV.security.jwt.JwtToken;
import br.senac.tads.pi.PI_IV.security.jwt.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<?> auth(@Valid @RequestBody UsuarioAuthRequestDTO personLoginDto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(personLoginDto.login(), personLoginDto.senha());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = userDetailsService.getAuthenticatedToken(personLoginDto.login());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.warn("Error", e.getMessage());
        }
        return ResponseEntity.badRequest().build();

    }


}
