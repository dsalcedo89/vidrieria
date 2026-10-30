package pe.com.vidrieria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoRequest(

        @NotBlank(message = "El código es obligatorio")
        @Size(max = 50, message = "El código no debe superar los 50 caracteres")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no debe superar los 150 caracteres")
        String nombre,

        @Size(max = 1000, message = "La descripción es demasiado extensa")
        String descripcion,

        @Size(max = 100, message = "La categoría no debe superar los 100 caracteres")
        String categoria,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message = "El stock no puede ser negativo")
        BigDecimal stock,

        @NotBlank(message = "La unidad de medida es obligatoria")
        @Size(max = 30, message = "La unidad de medida no debe superar los 30 caracteres")
        String unidadMedida,

        @Size(max = 500, message = "La URL de imagen es demasiado extensa")
        String imagenUrl

) {
}