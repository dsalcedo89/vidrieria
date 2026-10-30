package pe.com.vidrieria.dto;

import java.util.List;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        Long usuarioId,
        String nombre,
        String email,
        List<String> roles
) {
}