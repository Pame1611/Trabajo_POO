package cochera.util;

/**
 * Normalización de placas vehiculares peruanas (equivale al hook usePlacaInput.ts).
 *
 *  - Solo A-Z y 0-9, en mayúsculas.
 *  - Máximo 6 caracteres alfanuméricos.
 *  - Se guarda SIN guion ("ABC123") y se muestra CON guion ("ABC-123").
 */
public final class Placa {

    public static final int LONGITUD_MAXIMA = 6;

    private Placa() {
    }

    /** "abc-123 " -> "ABC123" */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String limpio = texto.toUpperCase().replaceAll("[^A-Z0-9]", "");
        return limpio.length() > LONGITUD_MAXIMA ? limpio.substring(0, LONGITUD_MAXIMA) : limpio;
    }

    /** "ABC123" -> "ABC-123" */
    public static String formatear(String texto) {
        String limpio = normalizar(texto);
        if (limpio.length() <= 3) {
            return limpio;
        }
        return limpio.substring(0, 3) + "-" + limpio.substring(3);
    }
}
