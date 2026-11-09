package cochera.modelo;

import java.time.LocalDateTime;

/**
 * Cajero de la cochera. Abre/cierra turno, registra entradas y cobra salidas.
 * Es el rol por defecto de todo usuario nuevo (trigger handle_new_user).
 */
public class Empleado extends Usuario {

    public Empleado(String email, String password, String nombreCompleto, LocalDateTime creadoEn) {
        super(email, password, nombreCompleto, creadoEn);
    }

    @Override
    public Rol getRol() {
        return Rol.EMPLEADO;
    }

    @Override
    public boolean puedeOperarCaja() {
        return true;
    }
}
