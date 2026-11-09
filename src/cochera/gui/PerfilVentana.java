package cochera.gui;

import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 * INTERFAZ (patrón Estrategia) que define qué muestra la ventana principal según el rol.
 * FrmPrincipal es un solo formulario; el perfil decide sus pestañas y su encabezado:
 *
 *   PerfilVentana perfil = usuario.esAdministrador() ? new PerfilAdministrador() : new PerfilCajero();
 */
public interface PerfilVentana {

    /** Título de la sección: "Caja" o "Administración". */
    String getTitulo();

    /** Agrega a la ventana las pestañas (formularios JPanel) de este rol. */
    void agregarPestanas(JTabbedPane pestanas, ContextoGui gui);

    /** Texto del indicador del encabezado (turno, vehículos dentro...). */
    String textoEstado(ContextoGui gui);

    Color colorEstado(ContextoGui gui);
}
