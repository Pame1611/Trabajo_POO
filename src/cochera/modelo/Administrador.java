package cochera.modelo;

import java.time.LocalDateTime;

/**
 * Dueño/administrador. Ve reportes, KPIs, auditoría de turnos, edita el tarifario
 * y corrige pagos. No tiene turno de caja, por lo tanto no cobra.
 */
public class Administrador extends Usuario {

    public Administrador(String email, String password, String nombreCompleto, LocalDateTime creadoEn) {
        super(email, password, nombreCompleto, creadoEn);
    }

    @Override
    public Rol getRol() {
        return Rol.ADMIN;
    }

    @Override
    public boolean puedeOperarCaja() {
        return false;
    }
}
