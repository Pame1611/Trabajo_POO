package cochera.impresion;

import cochera.modelo.Pago;
import cochera.modelo.TicketCochera;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.time.LocalDateTime;

/**
 * Ticket de SALIDA (comprobante interno de cobro) — construirTicketSalida() de lib/escpos.js.
 * También sirve para la reimpresión desde "Mis cobros".
 */
public class TicketSalida extends TicketImprimible {

    private final Pago pago;

    public TicketSalida(Pago pago) {
        this.pago = pago;
    }

    @Override
    public String getTitulo() {
        return "Ticket de salida " + pago.getTicket().getCodigoBarras();
    }

    /** Sobrecargo = total - tarifa base (nunca negativo). */
    public double getSobrecargo() {
        return Math.max(0, pago.getMontoTotal() - pago.getTicket().getTarifa().getCostoBase());
    }

    private LocalDateTime horaSalida() {
        TicketCochera t = pago.getTicket();
        return t.getHoraSalida() != null ? t.getHoraSalida() : pago.getCreadoEn();
    }

    @Override
    protected String encabezado() {
        return FormatoTermico.centro("TICKET DE SALIDA")
                + FormatoTermico.centro("- INTERNO -")
                + FormatoTermico.centro("COCHERA LOS ANGELES")
                + FormatoTermico.separador();
    }

    @Override
    protected String cuerpo() {
        TicketCochera t = pago.getTicket();
        long minutos = FechaUtil.minutosEntre(t.getHoraEntrada(), horaSalida());
        return FormatoTermico.fila("Correlativo:", t.getCodigoBarras())
                + FormatoTermico.fila("Placa:", t.getPlacaFormateada())
                + FormatoTermico.fila("Vehiculo:", t.getTarifa().getTipoVehiculo())
                + FormatoTermico.fila("Hora Entrada:", FechaUtil.hora(t.getHoraEntrada()))
                + FormatoTermico.fila("Hora Salida:", FechaUtil.hora(horaSalida()))
                + FormatoTermico.fila("Tiempo Total:", FechaUtil.formatoMinutos(minutos))
                + FormatoTermico.separador()
                + FormatoTermico.fila("Tarifa Base:", Dinero.soles(t.getTarifa().getCostoBase()))
                + FormatoTermico.fila("Sobrecargo:", Dinero.soles(getSobrecargo()))
                + FormatoTermico.centro("TOTAL: " + Dinero.soles(pago.getMontoTotal()))
                + FormatoTermico.separador();
    }

    @Override
    protected String pie() {
        return FormatoTermico.fila("Metodo Pago:", pago.getMetodoPago().getEtiqueta())
                + FormatoTermico.centro("!Gracias por su preferencia!");
    }
}
