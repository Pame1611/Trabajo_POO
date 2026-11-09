package cochera.servicio;

import cochera.dto.ResultadoTarifa;
import cochera.modelo.TicketCochera;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.time.LocalDateTime;

/**
 * FUENTE DE VERDAD ÚNICA del cálculo de tarifas (lib/tarifa.ts).
 *
 * Regla de "bloques de 12 horas" (aprobada 2026-07-15):
 *   k     = bloques completos de 12 h transcurridos.
 *   Si k == 0  -> se cobra la tarifa base (TB), sin importar cuánto tiempo pasó.
 *   Si k >= 1:
 *       hExt = horas restantes después del último bloque completo.
 *       Si hExt <= 0.5 (30 min de gracia) -> cRem = 0.
 *       Si hExt >  0.5 -> cRem = min(ceil(hExt) * S, TB).
 *       total = TB * k + cRem.
 */
public class CalculadoraTarifa {

    public static final double HORAS_POR_BLOQUE = 12.0;
    public static final double HORAS_DE_GRACIA = 0.5;

    public ResultadoTarifa calcular(LocalDateTime entrada, LocalDateTime ahora, double costoBase, double sobrecargoHora) {
        long minutos = Math.max(0, FechaUtil.minutosEntre(entrada, ahora));
        double t = minutos / 60.0;                         // horas totales (decimal)
        long k = (long) Math.floor(t / HORAS_POR_BLOQUE);  // bloques completos de 12 h
        double hExt = t - k * HORAS_POR_BLOQUE;            // horas del bloque en curso

        double total;
        double cRem = 0;
        int horasExtra = 0;

        if (k == 0) {
            total = costoBase;                             // menos de 12 h: tarifa base fija
        } else {
            if (hExt > HORAS_DE_GRACIA) {
                horasExtra = (int) Math.ceil(hExt);
                cRem = Math.min(horasExtra * sobrecargoHora, costoBase); // tope: no supera la tarifa base
            }
            total = costoBase * k + cRem;
        }
        return new ResultadoTarifa(minutos, Math.max(1, k), Dinero.redondear(total), horasExtra, Dinero.redondear(cRem));
    }

    /** Calcula con la tarifa vigente del tipo de vehículo del ticket. */
    public ResultadoTarifa calcular(TicketCochera ticket, LocalDateTime ahora) {
        return calcular(ticket.getHoraEntrada(), ahora,
                ticket.getTarifa().getCostoBase(), ticket.getTarifa().getSobrecargoHora());
    }
}
