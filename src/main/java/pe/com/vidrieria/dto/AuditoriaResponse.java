package pe.com.vidrieria.dto;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        Long usuarioId,
        String accion,
        String entidad,
        Long entidadId,
        String detalle,
        String direccionIp,
        LocalDateTime fechaEvento
) {
}