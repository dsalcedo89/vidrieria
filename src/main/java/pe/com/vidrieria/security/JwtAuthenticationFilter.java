package pe.com.vidrieria.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        System.out.println("========== JWT FILTER ==========");
        System.out.println("URL: " + request.getRequestURI());
        System.out.println("METHOD: " + request.getMethod());
        System.out.println("TOKEN PRESENTE: " +
                (header != null && header.startsWith("Bearer ")));

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            String email = jwtService.extraerEmail(token);

            System.out.println("EMAIL DEL TOKEN: " + email);

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails user = userDetailsService.loadUserByUsername(email);

                System.out.println("USUARIO: " + user.getUsername());
                System.out.println("AUTORIDADES: " + user.getAuthorities());

                boolean valido = jwtService.tokenValido(token, user);

                System.out.println("TOKEN VALIDO: " + valido);

                if (valido) {
                    var auth = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    auth.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContext context =
                            SecurityContextHolder.createEmptyContext();

                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);
                }
            }

        } catch (RuntimeException ex) {
            ex.printStackTrace();
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"mensaje\":\"Token inválido o expirado\"}");
            return;
        }

        System.out.println("================================");

        chain.doFilter(request, response);
    }

}
