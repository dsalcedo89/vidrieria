package pe.com.vidrieria.dto;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(
        @NotNull Boolean activo
) {}