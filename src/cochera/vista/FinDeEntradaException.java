package cochera.vista;

/**
 * Se lanza cuando ya no hay más entrada por teclado (por ejemplo, al ejecutar con un archivo de prueba).
 */
public class FinDeEntradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FinDeEntradaException() {
        super("Fin de la entrada");
    }
}
