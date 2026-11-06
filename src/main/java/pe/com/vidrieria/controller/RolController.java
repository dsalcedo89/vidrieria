package pe.com.vidrieria.controller;
import jakarta.validation.Valid;
import pe.com.vidrieria.dto.RolRequest;
import pe.com.vidrieria.dto.RolResponse;
import pe.com.vidrieria.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class RolController {
    private final RolService service;
    public RolController(RolService service) { this.service=service; }
    @GetMapping public List<RolResponse> listar() { return service.listar(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public RolResponse crear(@Valid @RequestBody RolRequest r) { return service.crear(r); }
    @PutMapping("/{id}") public RolResponse actualizar(@PathVariable Long id,@Valid @RequestBody RolRequest r) {
        return service.actualizar(id,r);
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id) { service.desactivar(id); }
}
