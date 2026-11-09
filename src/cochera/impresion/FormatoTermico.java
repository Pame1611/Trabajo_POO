package cochera.impresion;

import cochera.util.Texto;

/**
 * Helpers de maquetación para papel térmico de 58 mm (32 columnas).
 * Equivale a filaAlineada() y a los comandos de alineación de lib/escpos.js.
 */
public final class FormatoTermico {

    public static final int ANCHO = 32;

    private FormatoTermico() {
    }

    /**
     * Etiqueta a la izquierda y valor pegado a la derecha, en exactamente 32 columnas.
     * Si no caben, trunca el valor con "...".
     */
    public static String fila(String etiqueta, String valor) {
        String e = String.valueOf(etiqueta);
        String v = String.valueOf(valor);
        if (ANCHO - e.length() - v.length() < 1) {
            int maxValor = ANCHO - e.length() - 3;
            v = maxValor > 0 ? v.substring(0, maxValor) + "..." : "...";
        }
        int relleno = Math.max(0, ANCHO - e.length() - v.length());
        return e + Texto.repetir(' ', relleno) + v + "\n";
    }

    /** Texto centrado (puede tener varias líneas separadas por \n). */
    public static String centro(String texto) {
        StringBuilder sb = new StringBuilder();
        for (String linea : texto.split("\n", -1)) {
            sb.append(Texto.centrar(linea, ANCHO)).append("\n");
        }
        return sb.toString();
    }

    public static String derecha(String texto) {
        return Texto.izquierda(texto, ANCHO) + "\n";
    }

    public static String separador() {
        return Texto.repetir('-', ANCHO) + "\n";
    }

    /** Parte un texto largo en líneas de 32 columnas. */
    public static String partir(String texto) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i += ANCHO) {
            sb.append(texto, i, Math.min(texto.length(), i + ANCHO)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Representación ASCII de un código de barras CODE128 para la vista en consola.
     * (En la impresora real se enviaba el comando GS k 73.)
     */
    public static String codigoBarras(String dato) {
        StringBuilder barras = new StringBuilder("||");
        for (char c : dato.toCharArray()) {
            barras.append(((c >> 1) & 1) == 1 ? "||" : "| ");
        }
        barras.append("||");
        String linea = barras.toString();
        return centro(linea) + centro(linea) + centro(dato);
    }
}
