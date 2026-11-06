package pe.com.vidrieria.service;

import pe.com.vidrieria.dto.LoginRequest;
import pe.com.vidrieria.dto.LoginResponse;
import pe.com.vidrieria.entity.Usuario;
import pe.com.vidrieria.repository.UsuarioRepository;
import pe.com.vidrieria.repository.UsuarioRolRepository;
import pe.com.vidrieria.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarios;
    private final UsuarioRolRepository usuarioRoles;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UsuarioRepository usuarios,
                       UsuarioRolRepository usuarioRoles, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarios = usuarios;
        this.usuarioRoles = usuarioRoles;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email().trim(), request.password()));
        UserDetails principal = (UserDetails) auth.getPrincipal();
        Usuario usuario = usuarios.findByEmailIgnoreCase(principal.getUsername())
            .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        List<String> roles = usuarioRoles.findByUsuario_Id(usuario.getId()).stream()
            .filter(ur -> Boolean.TRUE.equals(ur.getRol().getActivo()))
            .map(ur -> ur.getRol().getNombre()).distinct().toList();
        return new LoginResponse(jwtService.generarToken(principal), "Bearer",
            jwtService.getExpirationMs() / 1000, usuario.getId(),
            usuario.getNombre(), usuario.getEmail(), roles);
    }
}
