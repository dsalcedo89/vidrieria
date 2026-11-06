package pe.com.vidrieria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CotizacionResponse(
        Long id,
        String numero,
        String nombreCliente,
        String emailCliente,
        String telefonoCliente,
        String descripcion,
        BigDecimal ancho,
        BigDecimal alto,
        Integer cantidad,
        String estado,
        String observaciones,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}