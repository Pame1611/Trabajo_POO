package cochera.excepcion;

/**
 * Excepción base del sistema (checked).
 * Todas las reglas de negocio que fallan lanzan esta excepción (o una subclase)
 * con un mensaje listo para mostrar al usuario.
 */
public class CocheraException extends Exception {

    private static final long serialVersionUID = 1L;

    public CocheraException(String mensaje) {
        super(mensaje);
    }

    public CocheraException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
