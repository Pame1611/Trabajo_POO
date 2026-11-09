package cochera.util;

/**
 * Helpers de texto compatibles con Java 8 (sin String.repeat).
 */
public final class Texto {

    private Texto() {
    }

    public static String repetir(char c, int veces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String derecha(String s, int ancho) {
        String v = s == null ? "" : s;
        return v.length() >= ancho ? v : v + repetir(' ', ancho - v.length());
    }

    public static String izquierda(String s, int ancho) {
        String v = s == null ? "" : s;
        return v.length() >= ancho ? v : repetir(' ', ancho - v.length()) + v;
    }

    /** Ajusta a un ancho fijo: rellena o corta con "..". */
    public static String columna(String s, int ancho) {
        String v = s == null ? "" : s;
        if (v.length() > ancho) {
            return ancho <= 2 ? v.substring(0, ancho) : v.substring(0, ancho - 2) + "..";
        }
        return derecha(v, ancho);
    }

    public static String centrar(String s, int ancho) {
        String v = s == null ? "" : s;
        if (v.length() >= ancho) {
            return v;
        }
        int izq = (ancho - v.length()) / 2;
        return repetir(' ', izq) + v;
    }

    public static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** Primeros 8 caracteres de un UUID, para mostrar en pantalla. */
    public static String idCorto(String id) {
        return id == null ? "-" : (id.length() > 8 ? id.substring(0, 8) : id);
    }
}
