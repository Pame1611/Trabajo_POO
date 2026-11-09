package cochera.gui;

import cochera.modelo.TurnoCaja;
import cochera.util.FechaUtil;
import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 * Pestañas del CAJERO: turno, check-in, check-out, autos dentro y sus cobros.
 */
public class PerfilCajero implements PerfilVentana {

    @Override
    public String getTitulo() {
        return "Caja";
    }

    @Override
    public void agregarPestanas(final JTabbedPane pestanas, ContextoGui gui) {
        final PnlCheckout checkout = new PnlCheckout(gui);
        pestanas.addTab("Turno de caja", new PnlTurno(gui));
        pestanas.addTab("Check-in (entrada)", new PnlCheckin(gui));
        pestanas.addTab("Check-out (salida)", checkout);
        // Desde "Autos dentro" se puede cobrar directo: se salta a Check-out con el ticket cargado.
        pestanas.addTab("Autos dentro", new PnlAutosDentro(gui, ticket -> {
            pestanas.setSelectedComponent(checkout);
            checkout.cargar(ticket.getCodigoBarras());
        }));
        pestanas.addTab("Mis cobros", new PnlMisCobros(gui));
    }

    @Override
    public String textoEstado(ContextoGui gui) {
        TurnoCaja turno = gui.getApp().getTurnos().obtenerTurnoAbierto();
        if (turno == null) {
            return "  SIN TURNO ABIERTO  ";
        }
        long minutos = FechaUtil.minutosEntre(turno.getFechaApertura(), gui.getApp().getReloj().ahora());
        return "  TURNO ABIERTO desde " + FechaUtil.hora(turno.getFechaApertura())
                + " (" + FechaUtil.formatoMinutos(minutos) + ")  ";
    }

    @Override
    public Color colorEstado(ContextoGui gui) {
        return gui.getApp().getTurnos().obtenerTurnoAbierto() == null ? Estilo.AMBAR : Estilo.VERDE;
    }
}
