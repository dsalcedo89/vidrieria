
package pe.com.vidrieria.controller;

import pe.com.vidrieria.dto.AuditoriaResponse;
import pe.com.vidrieria.repository.AuditoriaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auditoria")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaController(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @GetMapping
    public Page<AuditoriaResponse> listar(
            @PageableDefault(
                    size = 20,
                    sort = "fechaEvento",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return auditoriaRepository
                .findAllByOrderByFechaEventoDesc(pageable)
                .map(AuditoriaResponse::desdeEntidad);
    }

    @GetMapping("/usuario/{usuarioId}")
    public Page<AuditoriaResponse> listarPorUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 20) Pageable pageable) {

        return auditoriaRepository
                .findByUsuarioIdOrderByFechaEventoDesc(usuarioId, pageable)
                .map(AuditoriaResponse::desdeEntidad);
    }

    @GetMapping("/entidad/{entidad}")
    public Page<AuditoriaResponse> listarPorEntidad(
            @PathVariable String entidad,
            @PageableDefault(size = 20) Pageable pageable) {

        return auditoriaRepository
                .findByEntidadOrderByFechaEventoDesc(entidad, pageable)
                .map(AuditoriaResponse::desdeEntidad);
    }

    @GetMapping("/filtros")
    public Page<AuditoriaResponse> buscarConFiltros(

            @RequestParam(required = false) Long usuarioId,

            @RequestParam(required = false) String accion,

            @RequestParam(required = false) String entidad,

            @RequestParam(required = false)
            @DateTimeFormat(iso = ISO.DATE_TIME)
            LocalDateTime desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = ISO.DATE_TIME)
            LocalDateTime hasta,

            @PageableDefault(
                    size = 20,
                    sort = "fechaEvento",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return auditoriaRepository.buscarConFiltros(
                usuarioId,
                accion,
                entidad,
                desde,
                hasta,
                pageable
        ).map(AuditoriaResponse::desdeEntidad);
    }

}
