package cochera.excepcion;

/**
 * Error de validación de datos ingresados (placa, montos, fechas, etc.).
 */
public class ValidacionException extends CocheraException {

    private static final long serialVersionUID = 1L;

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
