package cochera.gui;

import cochera.excepcion.CocheraException;
import cochera.impresion.Impresora;
import java.awt.KeyboardFocusManager;
import java.awt.Window;

/**
 * Impresora térmica simulada para la versión gráfica: cada ticket "sale" en una
 * ventana con el papel de 58 mm (formulario DlgTicket).
 * Es otra implementación de la INTERFAZ Impresora, intercambiable con las demás.
 */
public class ImpresoraVentana implements Impresora {

    private boolean conectada;

    @Override
    public String getNombre() {
        return "Térmica 58 mm (ventana en pantalla)";
    }

    @Override
    public boolean estaConectada() {
        return conectada;
    }

    @Override
    public void conectar() {
        conectada = true;
    }

    @Override
    public void desconectar() {
        conectada = false;
    }

    @Override
    public void imprimir(String contenido) throws CocheraException {
        if (!conectada) {
            throw new CocheraException("La impresora no está conectada.");
        }
        Window duenio = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        new DlgTicket(duenio).mostrar(contenido);
    }
}
