package pe.com.vidrieria.dto;

import java.time.LocalDateTime;

public record RolResponse(
        Long id,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaCreacion
) {
}