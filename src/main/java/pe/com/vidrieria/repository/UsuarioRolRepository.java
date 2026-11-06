package pe.com.vidrieria.repository;

import pe.com.vidrieria.entity.UsuarioRol;
import pe.com.vidrieria.entity.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRolRepository
        extends JpaRepository<UsuarioRol, UsuarioRolId> {

    List<UsuarioRol> findByUsuario_Id(Long usuarioId);

    void deleteByUsuario_Id(Long usuarioId);
}