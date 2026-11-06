package pe.com.vidrieria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CotizacionRequest(

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String nombreCliente,

        @Email(message = "El correo no tiene un formato válido")
        String emailCliente,

        String telefonoCliente,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotNull(message = "El ancho es obligatorio")
        @Positive(message = "El ancho debe ser mayor que cero")
        @DecimalMin(value = "0.01")
        BigDecimal ancho,

        @NotNull(message = "El alto es obligatorio")
        @Positive(message = "El alto debe ser mayor que cero")
        @DecimalMin(value = "0.01")
        BigDecimal alto,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos uno")
        Integer cantidad,

        String observaciones

) {
}