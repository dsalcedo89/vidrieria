package pe.com.vidrieria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UsuarioRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String nombre,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100)
        String apellidos,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 150)
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 12, max = 72,
                message = "La contraseña debe tener entre 12 y 72 caracteres")
        String password,

        List<Long> rolesIds

) {
}