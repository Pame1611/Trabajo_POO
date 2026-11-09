package cochera.gui;

import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 * Pestañas del ADMINISTRADOR: KPIs, cierre 2 PM, auditoría, pagos, tarifario, exportación y usuarios.
 */
public class PerfilAdministrador implements PerfilVentana {

    @Override
    public String getTitulo() {
        return "Administración";
    }

    @Override
    public void agregarPestanas(JTabbedPane pestanas, ContextoGui gui) {
        pestanas.addTab("Resumen del día", new PnlResumen(gui));
        pestanas.addTab("Cierre 2 PM", new PnlCierre(gui));
        pestanas.addTab("Turnos cerrados", new PnlTurnosCerrados(gui));
        pestanas.addTab("Pagos", new PnlPagos(gui));
        pestanas.addTab("Tarifario", new PnlTarifario(gui));
        pestanas.addTab("Exportar a Excel", new PnlExportar(gui));
        pestanas.addTab("Autos dentro", new PnlAutosDentro(gui, null));
        pestanas.addTab("Usuarios", new PnlUsuarios(gui));
    }

    @Override
    public String textoEstado(ContextoGui gui) {
        return "  VEHÍCULOS DENTRO: " + gui.getApp().getBaseDatos().listarTicketsEnCurso().size() + "  ";
    }

    @Override
    public Color colorEstado(ContextoGui gui) {
        return Estilo.PRIMARIO;
    }
}
