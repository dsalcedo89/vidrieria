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
import java.util.Locale;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return convertirAResponse(obtenerProducto(id));
    }

    @Transactional
    public ProductoResponse registrar(ProductoRequest request) {

        String codigo = normalizarCodigo(request.codigo());

        if (productoRepository.existsByCodigo(codigo)) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con el código: " + codigo
            );
        }

        Producto producto = new Producto();

        aplicarDatos(producto, request);
        producto.setCodigo(codigo);
        producto.setActivo(true);

        return convertirAResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {

        Producto producto = obtenerProducto(id);
        String codigo = normalizarCodigo(request.codigo());

        productoRepository.findByCodigo(codigo)
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new ReglaNegocioException(
                            "El código ya pertenece a otro producto: " + codigo
                    );
                });

        aplicarDatos(producto, request);
        producto.setCodigo(codigo);

        return convertirAResponse(productoRepository.save(producto));
    }

    @Transactional
    public void desactivar(Long id) {

        Producto producto = obtenerProducto(id);
        producto.setActivo(false);

        productoRepository.save(producto);
    }

    private Producto obtenerProducto(Long id) {

        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el producto con ID: " + id
                ));
    }

    private void aplicarDatos(
            Producto producto,
            ProductoRequest request) {

        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion());
        producto.setCategoria(request.categoria());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setUnidadMedida(request.unidadMedida().trim());
        producto.setImagenUrl(request.imagenUrl());
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase(Locale.ROOT);
    }

    private ProductoResponse convertirAResponse(Producto producto) {

        return new ProductoResponse(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getUnidadMedida(),
                producto.getImagenUrl()
        );
    }
}