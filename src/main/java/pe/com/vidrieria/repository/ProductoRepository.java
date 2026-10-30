package pe.com.vidrieria.repository;

import pe.com.vidrieria.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrueOrderByNombreAsc();

    Optional<Producto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}