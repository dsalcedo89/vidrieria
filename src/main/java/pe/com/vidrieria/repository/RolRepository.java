package pe.com.vidrieria.repository;

import pe.com.vidrieria.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<Rol> findByNombreIgnoreCase(String nombre);

    List<Rol> findByActivoTrueOrderByNombreAsc();
}