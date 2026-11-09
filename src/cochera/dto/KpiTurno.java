package cochera.dto;

import cochera.modelo.TurnoCaja;

/**
 * KPIs del turno del cajero en la pantalla "Mis cobros" (KpiTurno en cobros.ts).
 */
public class KpiTurno {

    private final TurnoCaja turno;
    private final ResumenPagos resumen;
    private final int ticketsEnCurso;

    public KpiTurno(TurnoCaja turno, ResumenPagos resumen, int ticketsEnCurso) {
        this.turno = turno;
        this.resumen = resumen;
        this.ticketsEnCurso = ticketsEnCurso;
    }

    public TurnoCaja getTurno() {
        return turno;
    }

    public ResumenPagos getResumen() {
        return resumen;
    }

    public int getTicketsPagados() {
        return resumen.getCantidad();
    }

    /** Vehículos EN_CURSO en toda la cochera. */
    public int getTicketsEnCurso() {
        return ticketsEnCurso;
    }
}
