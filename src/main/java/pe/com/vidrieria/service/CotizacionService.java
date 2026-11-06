package pe.com.vidrieria.service;

import pe.com.vidrieria.dto.CotizacionRequest;
import pe.com.vidrieria.dto.CotizacionResponse;
import pe.com.vidrieria.entity.Cotizacion;
import pe.com.vidrieria.exception.RecursoNoEncontradoException;
import pe.com.vidrieria.repository.CotizacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CotizacionService {
    private final CotizacionRepository repo;
    public CotizacionService(CotizacionRepository repo) { this.repo = repo; }

    public CotizacionResponse crear(CotizacionRequest r) {
        Cotizacion c = new Cotizacion();
        c.setNumero("COT-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
        aplicar(c,r);
        c.setEstado("PENDIENTE");
        return response(repo.save(c));
    }
    @Transactional(readOnly = true)
    public List<CotizacionResponse> listar(String estado) {
        List<Cotizacion> datos = (estado == null || estado.isBlank())
            ? repo.findAllByOrderByFechaCreacionDesc()
            : repo.findByEstadoIgnoreCaseOrderByFechaCreacionDesc(estado.trim());
        return datos.stream().map(this::response).toList();
    }
    @Transactional(readOnly = true)
    public CotizacionResponse obtener(Long id) { return response(buscar(id)); }
    public CotizacionResponse cambiarEstado(Long id, String estado) {
        String e = estado == null ? "" : estado.trim().toUpperCase();
        if (!List.of("PENDIENTE","EN_REVISION","APROBADA","RECHAZADA","FINALIZADA").contains(e))
            throw new IllegalArgumentException("Estado no permitido.");
        Cotizacion c = buscar(id); c.setEstado(e); return response(repo.save(c));
    }
    private Cotizacion buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Cotización no encontrada: " + id));
    }
    private void aplicar(Cotizacion c, CotizacionRequest r) {
        c.setNombreCliente(r.nombreCliente().trim()); c.setEmailCliente(r.emailCliente());
        c.setTelefonoCliente(r.telefonoCliente()); c.setDescripcion(r.descripcion().trim());
        c.setAncho(r.ancho()); c.setAlto(r.alto()); c.setCantidad(r.cantidad());
        c.setObservaciones(r.observaciones());
    }
    private CotizacionResponse response(Cotizacion c) {
        return new CotizacionResponse(c.getId(),c.getNumero(),c.getNombreCliente(),c.getEmailCliente(),
            c.getTelefonoCliente(),c.getDescripcion(),c.getAncho(),c.getAlto(),c.getCantidad(),
            c.getEstado(),c.getObservaciones(),c.getFechaCreacion(),c.getFechaActualizacion());
    }
}
