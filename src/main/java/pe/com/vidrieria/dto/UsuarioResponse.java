package pe.com.vidrieria.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellidos,
        String email,
        Boolean activo,
        List<String> roles,
        LocalDateTime fechaCreacion
) {}