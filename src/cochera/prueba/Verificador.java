package cochera.prueba;

/**
 * Mini-herramienta de pruebas (sin JUnit): cuenta aciertos y fallos.
 */
public class Verificador {

    @FunctionalInterface
    public interface Accion {
        void ejecutar() throws Exception;
    }

    private int aciertos;
    private int fallos;

    public void verdadero(boolean condicion, String descripcion) {
        if (condicion) {
            aciertos++;
            System.out.println("  [PASA]  " + descripcion);
        } else {
            fallos++;
            System.out.println("  [FALLA] " + descripcion);
        }
    }

    public void igual(double esperado, double real, String descripcion) {
        verdadero(Math.abs(esperado - real) < 0.001, descripcion + " (esperado " + esperado + ", obtenido " + real + ")");
    }

    /** Verifica que la acción lance una excepción del tipo indicado. */
    public void lanza(Class<? extends Exception> tipo, Accion accion, String descripcion) {
        try {
            accion.ejecutar();
            verdadero(false, descripcion + " (no lanzo excepcion)");
        } catch (Exception e) {
            verdadero(tipo.isInstance(e), descripcion + " -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    public int getFallos() {
        return fallos;
    }

    public void resumen() {
        System.out.println();
        System.out.println("Resultado: " + aciertos + " pruebas pasan, " + fallos + " fallan.");
    }
}
