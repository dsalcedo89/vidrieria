package pe.com.vidrieria.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        String categoria,
        BigDecimal precio,
        BigDecimal stock,
        String unidadMedida,
        String imagenUrl
) {
}