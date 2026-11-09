package cochera.modelo;

import cochera.util.Dinero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cobro de un ticket (tabla pagos). Comprobante: ticket interno.
 * Siempre queda asociado al turno de quien COBRA (no de quien hizo el check-in).
 */
public class Pago {

    private final String id;
    private final TicketCochera ticket;
    private final TurnoCaja turno;
    private final double montoTotal;
    private final LocalDateTime creadoEn;
    private MetodoPago metodoPago;

    public Pago(TicketCochera ticket, TurnoCaja turno, double montoTotal, MetodoPago metodoPago, LocalDateTime creadoEn) {
        this.id = UUID.randomUUID().toString();
        this.ticket = ticket;
        this.turno = turno;
        this.montoTotal = Dinero.redondear(montoTotal);
        this.metodoPago = metodoPago;
        this.creadoEn = creadoEn;
    }

    /** Corrección de un error del cajero (admin, o el cajero con su turno aún abierto). */
    public void cambiarMetodoPago(MetodoPago nuevo) {
        this.metodoPago = nuevo;
    }

    public String getId() {
        return id;
    }

    public TicketCochera getTicket() {
        return ticket;
    }

    public TurnoCaja getTurno() {
        return turno;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
}
