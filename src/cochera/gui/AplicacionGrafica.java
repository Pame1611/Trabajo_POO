package cochera.gui;

import cochera.ContextoAplicacion;
import cochera.SistemaCochera;
import cochera.modelo.Usuario;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Arranque de la versión gráfica: muestra el login y, según el rol del usuario,
 * abre la ventana principal con el perfil correspondiente.
 */
public class AplicacionGrafica {

    private final ContextoGui gui;

    public AplicacionGrafica() {
        ContextoAplicacion app = new ContextoAplicacion(SistemaCochera.CARGAR_DATOS_DEMOSTRACION, new ImpresoraVentana());
        this.gui = new ContextoGui(app, this);
    }

    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            Estilo.instalarLookAndFeel();
            mostrarLogin();
        });
    }

    public void mostrarLogin() {
        new FrmLogin(gui).setVisible(true);
    }

    /** POLIMORFISMO: la ventana es la misma; el perfil (cajero o admin) cambia su contenido. */
    public void abrirSesion(Usuario usuario) {
        PerfilVentana perfil = usuario.esAdministrador() ? new PerfilAdministrador() : new PerfilCajero();
        new FrmPrincipal(gui, perfil).setVisible(true);
    }

    public void cerrarSesion(JFrame ventana) {
        gui.getApp().getAuth().cerrarSesion();
        gui.setOyenteCambios(null);
        ventana.dispose();
        mostrarLogin();
    }
}
