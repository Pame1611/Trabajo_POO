package cochera.gui;

import cochera.dto.ResultadoTarifa;
import cochera.modelo.MetodoPago;
import cochera.modelo.TicketCochera;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Mensajes y preguntas estándar (JOptionPane) con textos en español.
 */
public final class Dialogos {

    private static final String TITULO = "Cochera Los Ángeles";

    private Dialogos() {
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, TITULO, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component padre, String pregunta) {
        Object[] opciones = {"Sí", "No"};
        int r = JOptionPane.showOptionDialog(padre, pregunta, TITULO, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        return r == 0;
    }

    public static String pedirTexto(Component padre, String pregunta) {
        String r = JOptionPane.showInputDialog(padre, pregunta, TITULO, JOptionPane.QUESTION_MESSAGE);
        return r == null ? null : r.trim();
    }

    /** Pide un método de pago; devuelve null si se cancela. */
    public static MetodoPago elegirMetodoPago(Component padre, MetodoPago actual) {
        Object r = JOptionPane.showInputDialog(padre, "Método actual: " + actual + "\nNuevo método de pago:",
                TITULO, JOptionPane.QUESTION_MESSAGE, null, MetodoPago.values(), actual);
        return (MetodoPago) r;
    }

    /** Texto con el detalle de estadía y lo que debe pagar un vehículo. */
    public static String textoCotizacion(TicketCochera t, ResultadoTarifa r) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket: ").append(t.getCodigoBarras()).append('\n');
        sb.append("Placa: ").append(t.getPlacaFormateada()).append("  (").append(t.getTarifa().getTipoVehiculo()).append(")\n");
        sb.append("Ingreso: ").append(FechaUtil.fechaHora(t.getHoraEntrada())).append('\n');
        sb.append("Tiempo: ").append(FechaUtil.formatoMinutos(r.getMinutos())).append('\n');
        sb.append("Tarifa base: ").append(Dinero.soles(t.getTarifa().getCostoBase())).append(" x ")
                .append(r.getBloques()).append(" bloque(s) de 12 h\n");
        if (r.getHorasExtra() > 0) {
            sb.append("Horas extra: ").append(r.getHorasExtra()).append(" = ")
                    .append(Dinero.soles(r.getMontoSobrecargo())).append('\n');
        }
        sb.append("\nTOTAL A COBRAR: ").append(Dinero.soles(r.getTarifaTotal()));
        return sb.toString();
    }
}
