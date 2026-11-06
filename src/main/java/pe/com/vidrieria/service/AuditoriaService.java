
package pe.com.vidrieria.service;

import pe.com.vidrieria.entity.Auditoria;
import pe.com.vidrieria.entity.Usuario;
import pe.com.vidrieria.repository.AuditoriaRepository;
import pe.com.vidrieria.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObjectProvider<HttpServletRequest> requestProvider;

    public AuditoriaService(
            AuditoriaRepository auditoriaRepository,
            UsuarioRepository usuarioRepository,
            ObjectProvider<HttpServletRequest> requestProvider) {

        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.requestProvider = requestProvider;
    }

    @Transactional
    public void registrarOperacion(
            String accion,
            String entidad,
            String entidadId,
            String detalle) {

        Auditoria auditoria = new Auditoria();

        // Identificar al usuario autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof
                org.springframework.security.core.userdetails.UserDetails) {

            String email = authentication.getName();

            Usuario usuario = usuarioRepository
                    .findByEmailIgnoreCase(email)
                    .orElse(null);

            if (usuario != null) {
                auditoria.setUsuarioId(usuario.getId());
            }
        }

        // Datos de la operación
        auditoria.setAccion(accion);
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setDetalle(detalle);

        // Dirección IP
        HttpServletRequest request = requestProvider.getIfAvailable();

        if (request != null) {
            auditoria.setDireccionIp(request.getRemoteAddr());
        }

        auditoriaRepository.save(auditoria);
    }
}
