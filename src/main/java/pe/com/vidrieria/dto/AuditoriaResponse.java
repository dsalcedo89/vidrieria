
package pe.com.vidrieria.dto;

import pe.com.vidrieria.entity.Auditoria;
import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        Long usuarioId,
        String accion,
        String entidad,
        String entidadId,
        String detalle,
        String direccionIp,
        LocalDateTime fechaEvento
) {
    public static AuditoriaResponse desdeEntidad(Auditoria a) {
        return new AuditoriaResponse(
                a.getId(),
                a.getUsuarioId(),
                a.getAccion(),
                a.getEntidad(),
                a.getEntidadId(),
                a.getDetalle(),
                a.getDireccionIp(),
                a.getFechaEvento()
        );
    }
}
