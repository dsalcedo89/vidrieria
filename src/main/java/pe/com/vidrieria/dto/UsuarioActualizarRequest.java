package pe.com.vidrieria.dto;

import jakarta.validation.constraints.*;
import java.util.Set;

public record UsuarioActualizarRequest(

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 150)
        String apellidos,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @Size(min = 8, max = 100)
        String password,

        @NotEmpty
        Set<Long> roles

) {}