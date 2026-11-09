package cochera.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Reloj del sistema en hora de Lima (America/Lima, UTC-5).
 *
 * Todas las clases piden la hora a este objeto (nunca a LocalDateTime.now() directamente),
 * así en la demostración se puede "adelantar el tiempo" para mostrar los cobros
 * por bloques de 12 horas sin esperar 12 horas reales.
 */
public class Reloj {

    public static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    private long minutosDesfase;

    /** Hora actual de Lima (más el desfase de demostración, si existe). */
    public LocalDateTime ahora() {
        return LocalDateTime.now(ZONA_LIMA).plusMinutes(minutosDesfase).withNano(0);
    }

    public void adelantarMinutos(long minutos) {
        if (minutos < 0) {
            throw new IllegalArgumentException("Solo se puede adelantar el reloj.");
        }
        minutosDesfase += minutos;
    }

    public void reiniciar() {
        minutosDesfase = 0;
    }

    public long getMinutosDesfase() {
        return minutosDesfase;
    }
}
