package pe.com.vidrieria.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AsignarRolesRequest(

        @NotEmpty(message = "Debe indicar al menos un rol")
        List<@NotNull Long> rolesIds

) {
}