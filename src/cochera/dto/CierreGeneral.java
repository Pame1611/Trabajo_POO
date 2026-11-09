package cochera.dto;

import cochera.modelo.Pago;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Cierre general operativo de 24 h con corte a las 2 PM (CierreGeneralData en admin.ts).
 * Va desde las 14:00:00 del día anterior hasta las 13:59:59 del día de corte.
 */
public class CierreGeneral {

    private final LocalDate fechaCorte;
    private final LocalDateTime desde;
    private final LocalDateTime hasta;
    private final ResumenPagos resumen;
    private final List<ReporteTurno> turnos;
    private final List<Pago> pagos;

    public CierreGeneral(LocalDate fechaCorte, LocalDateTime desde, LocalDateTime hasta, ResumenPagos resumen,
                         List<ReporteTurno> turnos, List<Pago> pagos) {
        this.fechaCorte = fechaCorte;
        this.desde = desde;
        this.hasta = hasta;
        this.resumen = resumen;
        this.turnos = turnos;
        this.pagos = pagos;
    }

    public double getTicketPromedio() {
        return resumen.getCantidad() == 0 ? 0 : Math.round(resumen.getTotal() / resumen.getCantidad() * 100.0) / 100.0;
    }

    public LocalDate getFechaCorte() {
        return fechaCorte;
    }

    public LocalDateTime getDesde() {
        return desde;
    }

    public LocalDateTime getHasta() {
        return hasta;
    }

    public ResumenPagos getResumen() {
        return resumen;
    }

    public List<ReporteTurno> getTurnos() {
        return turnos;
    }

    public List<Pago> getPagos() {
        return pagos;
    }
}
