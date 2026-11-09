package cochera.dto;

import cochera.modelo.TurnoCaja;

/**
 * Turno de caja con el monto recaudado (total del turno o lo cobrado dentro de un corte).
 */
public class ReporteTurno {

    private final TurnoCaja turno;
    private final double recaudado;

    public ReporteTurno(TurnoCaja turno, double recaudado) {
        this.turno = turno;
        this.recaudado = recaudado;
    }

    public TurnoCaja getTurno() {
        return turno;
    }

    public double getRecaudado() {
        return recaudado;
    }
}
