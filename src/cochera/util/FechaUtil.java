package cochera.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utilidades de fecha y hora (zona Lima). Equivale a los helpers
 * formatHora / formatFecha / formatMinutos de los componentes React.
 */
public final class FechaUtil {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FECHA_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private FechaUtil() {
    }

    public static String fecha(LocalDateTime f) {
        return f == null ? "-" : f.format(FECHA);
    }

    public static String hora(LocalDateTime f) {
        return f == null ? "-" : f.format(HORA);
    }

    public static String fechaHora(LocalDateTime f) {
        return f == null ? "-" : f.format(FECHA_HORA);
    }

    public static String fechaIso(LocalDateTime f) {
        return f == null ? "" : f.format(FECHA_ISO);
    }

    public static String fechaIso(LocalDate f) {
        return f == null ? "" : f.format(FECHA_ISO);
    }

    /** Minutos completos entre dos instantes (redondeo hacia abajo, como Math.floor en JS). */
    public static long minutosEntre(LocalDateTime desde, LocalDateTime hasta) {
        return Math.floorDiv(Duration.between(desde, hasta).getSeconds(), 60L);
    }

    /** "45 min", "3h", "2h 35m" (formatMinutos del CheckoutForm). */
    public static String formatoMinutos(long minutos) {
        long h = minutos / 60;
        long m = minutos % 60;
        if (h == 0) {
            return m + " min";
        }
        if (m == 0) {
            return h + "h";
        }
        return h + "h " + m + "m";
    }

    /** "3h 05m", "12m 09s", "40s" (TiempoTranscurrido de Autos Dentro). */
    public static String tiempoTranscurrido(LocalDateTime entrada, LocalDateTime ahora) {
        long seg = Math.max(0, Duration.between(entrada, ahora).getSeconds());
        long h = seg / 3600;
        long m = (seg % 3600) / 60;
        long s = seg % 60;
        if (h > 0) {
            return h + "h " + dosDigitos(m) + "m";
        }
        if (m > 0) {
            return m + "m " + dosDigitos(s) + "s";
        }
        return s + "s";
    }

    /** Acepta "2026-11-02" o "02/11/2026". */
    public static LocalDate parsearFecha(String texto) throws DateTimeParseException {
        String t = texto.trim();
        if (t.contains("/")) {
            return LocalDate.parse(t, FECHA);
        }
        return LocalDate.parse(t, FECHA_ISO);
    }

    private static String dosDigitos(long n) {
        return n < 10 ? "0" + n : String.valueOf(n);
    }
}
