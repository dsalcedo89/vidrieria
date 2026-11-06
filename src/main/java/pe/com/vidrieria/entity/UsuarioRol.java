package pe.com.vidrieria.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_roles")
public class UsuarioRol {

    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rolId")
    @JoinColumn(name = "rol_id")
    private Rol rol;

    public UsuarioRol() {}

    public UsuarioRol(Usuario usuario, Rol rol) {
        this.usuario = usuario;
        this.rol = rol;
        this.id = new UsuarioRolId(usuario.getId(), rol.getId());
    }

    public UsuarioRolId getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Rol getRol() { return rol; }
}