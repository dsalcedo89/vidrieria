
package pe.com.vidrieria.service;

import pe.com.vidrieria.dto.*;
import pe.com.vidrieria.entity.*;
import pe.com.vidrieria.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            UsuarioRolRepository usuarioRolRepository,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return convertirRespuesta(obtenerUsuario(id));
    }

    // CREAR USUARIO
    public UsuarioResponse registrar(UsuarioRequest request) {

        String email = request.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException(
                    "El correo electrónico ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre().trim());
        usuario.setApellidos(request.apellidos().trim());
        usuario.setEmail(email);

        usuario.setPasswordHash(
                passwordEncoder.encode(request.password()));

        usuario.setActivo(true);

        usuario = usuarioRepository.save(usuario);

        asignarRoles(usuario, request.roles());

        auditoriaService.registrarOperacion(
                "CREAR",
                "Usuario",
                usuario.getId().toString(),
                "Se creó el usuario: " + usuario.getEmail()
        );

        return convertirRespuesta(usuario);
    }

    // ACTUALIZAR USUARIO
    public UsuarioResponse actualizar(
            Long id, UsuarioActualizarRequest request) {

        Usuario usuario = obtenerUsuario(id);

        String email = request.email().trim().toLowerCase();

        usuarioRepository.findByEmailIgnoreCase(email)
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "El correo electrónico pertenece a otro usuario");
                });

        usuario.setNombre(request.nombre().trim());
        usuario.setApellidos(request.apellidos().trim());
        usuario.setEmail(email);

        boolean cambioPassword =
                request.password() != null
                        && !request.password().isBlank();

        if (cambioPassword) {
            usuario.setPasswordHash(
                    passwordEncoder.encode(request.password()));
        }

        usuarioRepository.save(usuario);

        asignarRoles(usuario, request.roles());

        auditoriaService.registrarOperacion(
                "ACTUALIZAR",
                "Usuario",
                usuario.getId().toString(),
                "Se actualizaron los datos del usuario: " + usuario.getEmail()
        );

        if (cambioPassword) {
            auditoriaService.registrarOperacion(
                    "CAMBIAR_PASSWORD",
                    "Usuario",
                    usuario.getId().toString(),
                    "Se modificó la contraseña del usuario: "
                            + usuario.getEmail()
            );
        }

        return convertirRespuesta(usuario);
    }

    // CAMBIAR ESTADO
    public UsuarioResponse cambiarEstado(
            Long id, CambiarEstadoRequest request) {

        Usuario usuario = obtenerUsuario(id);

        Boolean estadoAnterior = usuario.getActivo();

        usuario.setActivo(request.activo());

        usuarioRepository.save(usuario);

        auditoriaService.registrarOperacion(
                "CAMBIAR_ESTADO",
                "Usuario",
                usuario.getId().toString(),
                "Se cambió el estado del usuario "
                        + usuario.getEmail()
                        + " de " + estadoAnterior
                        + " a " + request.activo()
        );

        return convertirRespuesta(usuario);
    }

    // CAMBIAR ROLES
    public UsuarioResponse cambiarRoles(
            Long id, CambiarRolesRequest request) {

        Usuario usuario = obtenerUsuario(id);

        asignarRoles(usuario, request.roles());

        auditoriaService.registrarOperacion(
                "CAMBIAR_ROLES",
                "Usuario",
                usuario.getId().toString(),
                "Se actualizaron los roles del usuario: "
                        + usuario.getEmail()
        );

        return convertirRespuesta(usuario);
    }

    // CAMBIAR CONTRASEÑA
    public void cambiarPassword(
            Long id, CambiarPasswordRequest request) {

        Usuario usuario = obtenerUsuario(id);

        usuario.setPasswordHash(
                passwordEncoder.encode(request.password()));

        usuarioRepository.save(usuario);

        auditoriaService.registrarOperacion(
                "CAMBIAR_PASSWORD",
                "Usuario",
                usuario.getId().toString(),
                "Se modificó la contraseña del usuario: "
                        + usuario.getEmail()
        );
    }

    // ASIGNAR ROLES
    private void asignarRoles(
            Usuario usuario, Set<Long> idsRoles) {

        List<Rol> roles = rolRepository.findAllById(idsRoles);

        if (roles.size() != idsRoles.size()) {
            throw new IllegalArgumentException(
                    "Uno o más roles no existen");
        }

        if (roles.stream().anyMatch(
                r -> !Boolean.TRUE.equals(r.getActivo()))) {

            throw new IllegalArgumentException(
                    "No se pueden asignar roles inactivos");
        }

        usuarioRolRepository.deleteByUsuario_Id(usuario.getId());

        List<UsuarioRol> relaciones = roles.stream()
                .map(rol -> new UsuarioRol(usuario, rol))
                .toList();

        usuarioRolRepository.saveAll(relaciones);
    }

    private Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el usuario con ID: " + id));
    }

    private UsuarioResponse convertirRespuesta(Usuario usuario) {

        List<String> roles = usuarioRolRepository
                .findByUsuario_Id(usuario.getId())
                .stream()
                .map(ur -> ur.getRol().getNombre())
                .distinct()
                .toList();

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getActivo(),
                roles,
                usuario.getFechaCreacion()
        );
    }
}
