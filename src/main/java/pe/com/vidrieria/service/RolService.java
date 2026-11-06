package pe.com.vidrieria.service;

import pe.com.vidrieria.dto.RolRequest;
import pe.com.vidrieria.dto.RolResponse;
import pe.com.vidrieria.entity.Rol;
import pe.com.vidrieria.exception.RecursoNoEncontradoException;
import pe.com.vidrieria.exception.ReglaNegocioException;
import pe.com.vidrieria.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class RolService {
    private final RolRepository repo;
    public RolService(RolRepository repo) { this.repo = repo; }

    @Transactional(readOnly=true)
    public List<RolResponse> listar() { return repo.findAll().stream().map(this::dto).toList(); }

    public RolResponse crear(RolRequest r) {
        String nombre = r.nombre().trim().toUpperCase();
        if (repo.existsByNombreIgnoreCase(nombre)) throw new ReglaNegocioException("El rol ya existe.");
        Rol rol = new Rol(); rol.setNombre(nombre); rol.setDescripcion(r.descripcion()); rol.setActivo(true);
        return dto(repo.save(rol));
    }
    public RolResponse actualizar(Long id, RolRequest r) {
        Rol rol = repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        String nombre = r.nombre().trim().toUpperCase();
        repo.findByNombreIgnoreCase(nombre).filter(x -> !x.getId().equals(id))
            .ifPresent(x -> { throw new ReglaNegocioException("El nombre del rol ya existe."); });
        rol.setNombre(nombre); rol.setDescripcion(r.descripcion()); return dto(repo.save(rol));
    }
    public void desactivar(Long id) {
        Rol rol = repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Rol no encontrado."));
        rol.setActivo(false); repo.save(rol);
    }
    private RolResponse dto(Rol r) { return new RolResponse(r.getId(),r.getNombre(),r.getDescripcion(),r.getActivo(),r.getFechaCreacion()); }
}
