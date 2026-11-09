package cochera.excepcion;

/**
 * Se lanza cuando el usuario no tiene sesión, no tiene el rol requerido
 * o no tiene un turno de caja abierto (equivalente a las políticas RLS de Supabase).
 */
public class AccesoDenegadoException extends CocheraException {

    private static final long serialVersionUID = 1L;

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
