package cochera.impresion;

import cochera.modelo.TicketCochera;
import cochera.util.Dinero;
import cochera.util.FechaUtil;

/**
 * Ticket de ENTRADA que se entrega al conductor en el check-in
 * (construirTicketEntrada() de lib/escpos.js).
 */
public class TicketEntrada extends TicketImprimible {

    private final TicketCochera ticket;

    public TicketEntrada(TicketCochera ticket) {
        this.ticket = ticket;
    }

    @Override
    public String getTitulo() {
        return "Ticket de ingreso " + ticket.getCodigoBarras();
    }

    @Override
    protected String encabezado() {
        return FormatoTermico.derecha("N.: " + ticket.getCodigoBarras())
                + FormatoTermico.centro("TICKET-INGRESO")
                + FormatoTermico.centro("REPRESENTACIONES E INVERSIONES\nLOS ANGELES SAC\n"
                        + "AV. JOSE GABRIEL CONDORCANQUI\nNRO. 1413 LA ESPERANZA-TRUJILLO")
                + "\n";
    }

    @Override
    protected String cuerpo() {
        return FormatoTermico.centro(ticket.getPlacaFormateada())
                + "\n"
                + FormatoTermico.fila("FECHA", FechaUtil.fecha(ticket.getHoraEntrada()))
                + FormatoTermico.fila("INGRESO", FechaUtil.hora(ticket.getHoraEntrada()))
                + "\n"
                + FormatoTermico.centro(ticket.getTarifa().getTipoVehiculo() + " S/."
                        + Dinero.decimal(ticket.getTarifa().getCostoBase()))
                + FormatoTermico.centro("TURNO O FRACCION")
                + "\n"
                + FormatoTermico.codigoBarras(ticket.getCodigoBarras());
    }

    @Override
    protected String pie() {
        return "Condiciones:\n"
                + "- No perder el ticket\n"
                + "* Pasando las 12 horas se cobra\n"
                + "un turno mas o fraccion.\n";
    }
}
