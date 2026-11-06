package pe.com.vidrieria.repository;
import pe.com.vidrieria.entity.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    Optional<Cotizacion> findByNumero(String numero);
    boolean existsByNumero(String numero);
    List<Cotizacion> findAllByOrderByFechaCreacionDesc();
    List<Cotizacion> findByEstadoIgnoreCaseOrderByFechaCreacionDesc(String estado);
}
