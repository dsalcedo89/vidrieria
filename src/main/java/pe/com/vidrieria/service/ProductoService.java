
package pe.com.vidrieria.service;

import pe.com.vidrieria.dto.ProductoRequest;
import pe.com.vidrieria.dto.ProductoResponse;
import pe.com.vidrieria.entity.Producto;
import pe.com.vidrieria.exception.RecursoNoEncontradoException;
import pe.com.vidrieria.exception.ReglaNegocioException;
import pe.com.vidrieria.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repo;
    private final AuditoriaService auditoriaService;

    public ProductoService(
            ProductoRepository repo,
            AuditoriaService auditoriaService) {

        this.repo = repo;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProductosActivos() {
        return repo.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(this::response)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return repo.findAll()
                .stream()
                .map(this::response)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtener(Long id) {
        return response(buscar(id));
    }

    // CREAR PRODUCTO
    public ProductoResponse crear(ProductoRequest r) {

        if (repo.existsByCodigo(r.codigo().trim())) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con ese código.");
        }

        Producto p = new Producto();

        aplicar(p, r);
        p.setActivo(true);

        p = repo.save(p);

        auditoriaService.registrarOperacion(
                "CREAR",
                "Producto",
                p.getId().toString(),
                "Se creó el producto: "
                        + p.getCodigo() + " - " + p.getNombre()
        );

        return response(p);
    }

    // ACTUALIZAR PRODUCTO
    public ProductoResponse actualizar(
            Long id, ProductoRequest r) {

        Producto p = buscar(id);

        repo.findByCodigo(r.codigo().trim())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new ReglaNegocioException(
                            "El código ya pertenece a otro producto.");
                });

        aplicar(p, r);

        repo.save(p);

        auditoriaService.registrarOperacion(
                "ACTUALIZAR",
                "Producto",
                p.getId().toString(),
                "Se actualizaron los datos del producto: "
                        + p.getCodigo() + " - " + p.getNombre()
        );

        return response(p);
    }

    // DESACTIVAR PRODUCTO
    public void desactivar(Long id) {

        Producto p = buscar(id);

        p.setActivo(false);

        repo.save(p);

        auditoriaService.registrarOperacion(
                "DESACTIVAR",
                "Producto",
                p.getId().toString(),
                "Se desactivó el producto: "
                        + p.getCodigo() + " - " + p.getNombre()
        );
    }

    private Producto buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Producto no encontrado: " + id));
    }

    private void aplicar(Producto p, ProductoRequest r) {

        p.setCodigo(r.codigo().trim());
        p.setNombre(r.nombre().trim());
        p.setDescripcion(r.descripcion());

        p.setCategoria(
                r.categoria() == null
                        ? "GENERAL"
                        : r.categoria().trim()
        );

        p.setPrecio(r.precio());
        p.setStock(r.stock());
        p.setUnidadMedida(r.unidadMedida().trim());
        p.setImagenUrl(r.imagenUrl());
    }

    private ProductoResponse response(Producto p) {

        return new ProductoResponse(
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getDescripcion(),
                p.getCategoria(),
                p.getPrecio(),
                p.getStock(),
                p.getUnidadMedida(),
                p.getImagenUrl()
        );
    }
}
