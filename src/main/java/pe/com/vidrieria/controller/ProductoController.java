package pe.com.vidrieria.controller;

import jakarta.validation.Valid;
import pe.com.vidrieria.dto.ProductoRequest;
import pe.com.vidrieria.dto.ProductoResponse;
import pe.com.vidrieria.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }

    @GetMapping public List<ProductoResponse> listar() { return service.listarProductosActivos(); }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    public List<ProductoResponse> listarTodos() { return service.listarTodos(); }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) { return service.crear(request); }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void desactivar(@PathVariable Long id) { service.desactivar(id); }
}
