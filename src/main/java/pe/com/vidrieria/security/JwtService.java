package pe.com.vidrieria.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
        @Value("${app.jwt.secret}") String secret,
        @Value("${app.jwt.expiration-ms:3600000}") long expirationMs) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("app.jwt.secret debe tener al menos 32 bytes.");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generarToken(UserDetails user) {
        Date ahora = new Date();
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(ahora)
            .expiration(new Date(ahora.getTime() + expirationMs))
            .signWith(key)
            .compact();
    }

    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public boolean tokenValido(String token, UserDetails user) {
        try {
            return user.getUsername().equals(extraerEmail(token)) &&
                extraerClaim(token, Claims::getExpiration).after(new Date());
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public long getExpirationMs() { return expirationMs; }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }
}
