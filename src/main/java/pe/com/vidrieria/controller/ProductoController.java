package pe.com.vidrieria.controller;

import pe.com.vidrieria.dto.ProductoResponse;
import pe.com.vidrieria.service.ProductoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponse> listarProductos() {
        return productoService.listarProductosActivos();
    }
}