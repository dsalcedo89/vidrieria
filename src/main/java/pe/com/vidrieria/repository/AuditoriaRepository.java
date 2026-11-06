
package pe.com.vidrieria.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.vidrieria.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AuditoriaRepository
        extends JpaRepository<Auditoria, Long> {

    Page<Auditoria> findAllByOrderByFechaEventoDesc(Pageable pageable);

    Page<Auditoria> findByUsuarioIdOrderByFechaEventoDesc(
            Long usuarioId, Pageable pageable);

    Page<Auditoria> findByEntidadOrderByFechaEventoDesc(
            String entidad, Pageable pageable);


    @Query("""
    SELECT a
    FROM Auditoria a
    WHERE (:usuarioId IS NULL OR a.usuarioId = :usuarioId)
      AND (:accion IS NULL OR UPPER(a.accion) = UPPER(:accion))
      AND (:entidad IS NULL OR UPPER(a.entidad) = UPPER(:entidad))
      AND (
          CAST(:desde AS timestamp) IS NULL
          OR a.fechaEvento >= :desde
      )
      AND (
          CAST(:hasta AS timestamp) IS NULL
          OR a.fechaEvento <= :hasta
      )
    ORDER BY a.fechaEvento DESC
    """)
    Page<Auditoria> buscarConFiltros(
            @Param("usuarioId") Long usuarioId,
            @Param("accion") String accion,
            @Param("entidad") String entidad,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta,
            Pageable pageable
    );
}
