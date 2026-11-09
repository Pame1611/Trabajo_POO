package cochera.dto;

import cochera.modelo.TicketCochera;
import java.time.LocalDateTime;

/**
 * Ticket EN_CURSO encontrado en el check-out con su tarifa calculada al momento
 * (TicketEncontrado en app/actions/checkout.ts).
 */
public class CotizacionSalida {

    private final TicketCochera ticket;
    private final ResultadoTarifa tarifa;
    private final LocalDateTime calculadoEn;

    public CotizacionSalida(TicketCochera ticket, ResultadoTarifa tarifa, LocalDateTime calculadoEn) {
        this.ticket = ticket;
        this.tarifa = tarifa;
        this.calculadoEn = calculadoEn;
    }

    public TicketCochera getTicket() {
        return ticket;
    }

    public ResultadoTarifa getTarifa() {
        return tarifa;
    }

    public LocalDateTime getCalculadoEn() {
        return calculadoEn;
    }
}
