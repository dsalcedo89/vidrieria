package pe.com.vidrieria.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CambiarRolesRequest(
        @NotEmpty Set<Long> roles
) {}