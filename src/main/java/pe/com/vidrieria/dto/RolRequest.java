package pe.com.vidrieria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolRequest(

        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 50)
        String nombre,

        @Size(max = 250)
        String descripcion

) {
}