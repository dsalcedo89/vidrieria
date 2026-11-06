
package pe.com.vidrieria.security;

import pe.com.vidrieria.entity.Usuario;
import pe.com.vidrieria.repository.UsuarioRepository;
import pe.com.vidrieria.repository.UsuarioRolRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarios;
    private final UsuarioRolRepository usuarioRoles;

    public CustomUserDetailsService(
            UsuarioRepository usuarios,
            UsuarioRolRepository usuarioRoles) {
        this.usuarios = usuarios;
        this.usuarioRoles = usuarioRoles;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarios.findByEmailIgnoreCase(email.trim())
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .orElseThrow(() ->
                        new UsernameNotFoundException("Credenciales inválidas"));

        var authorities = usuarioRoles.findByUsuario_Id(usuario.getId())
                .stream()
                .filter(ur ->
                        Boolean.TRUE.equals(ur.getRol().getActivo()))
                .map(ur -> new SimpleGrantedAuthority(
                        "ROLE_" + ur.getRol().getNombre().toUpperCase()))
                .collect(Collectors.toSet());

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .authorities(authorities)
                .build();
    }
}
