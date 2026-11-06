package pe.com.vidrieria.controller;

import jakarta.validation.Valid;
import pe.com.vidrieria.dto.CotizacionRequest;
import pe.com.vidrieria.dto.CotizacionResponse;
import pe.com.vidrieria.service.CotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {
    private final CotizacionService service;
    public CotizacionController(CotizacionService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CotizacionResponse crear(@Valid @RequestBody CotizacionRequest r) { return service.crear(r); }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    public List<CotizacionResponse> listar(@RequestParam(required=false) String estado) { return service.listar(estado); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    public CotizacionResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    public CotizacionResponse cambiarEstado(@PathVariable Long id, @RequestBody Map<String,String> body) {
        return service.cambiarEstado(id, body.get("estado"));
    }
}
