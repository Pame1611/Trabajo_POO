package cochera.util;

import java.util.Locale;

/**
 * Utilidades para montos en soles.
 */
public final class Dinero {

    private Dinero() {
    }

    /** Redondea a 2 decimales . */
    public static double redondear(double monto) {
        return Math.round(monto * 100.0) / 100.0;
    }

    /** 12.5 -> "12.50" (siempre con punto decimal). */
    public static String decimal(double monto) {
        return String.format(Locale.US, "%.2f", monto);
    }

    /** 12.5 -> "S/ 12.50" */
    public static String soles(double monto) {
        return "S/ " + decimal(monto);
    }

    /** Diferencia con signo: +S/ 2.00 / -S/ 1.50 */
    public static String conSigno(double monto) {
        return (monto >= 0 ? "+" : "-") + soles(Math.abs(monto));
    }
}
