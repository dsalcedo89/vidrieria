package pe.com.vidrieria.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioRolId implements Serializable {

    private Long usuarioId;
    private Long rolId;

    public UsuarioRolId() {
    }

    public UsuarioRolId(Long usuarioId, Long rolId) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof UsuarioRolId otro)) {
            return false;
        }

        return Objects.equals(usuarioId, otro.usuarioId)
                && Objects.equals(rolId, otro.rolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, rolId);
    }
}