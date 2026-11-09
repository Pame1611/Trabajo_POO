package cochera.impresion;

/**
 * Documento que se imprime en la térmica de 58 mm (clase ABSTRACTA).
 *
 *          TicketImprimible
 *            /          \
 *     TicketEntrada   TicketSalida
 *
 * Patrón "Método Plantilla": generar() fija el orden encabezado → cuerpo → pie,
 * y cada subclase decide QUÉ va en cada parte (polimorfismo).
 */
public abstract class TicketImprimible {

    /** Método plantilla: no se puede sobrescribir (final). */
    public final String generar() {
        return encabezado() + cuerpo() + pie();
    }

    public abstract String getTitulo();

    protected abstract String encabezado();

    protected abstract String cuerpo();

    protected abstract String pie();

    @Override
    public String toString() {
        return getTitulo();
    }
}
