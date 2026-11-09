package cochera.gui;

import cochera.ContextoAplicacion;
import cochera.excepcion.CocheraException;
import cochera.impresion.Impresora;
import cochera.impresion.TicketImprimible;
import java.awt.Component;

/**
 * Lo que comparten todos los formularios: los servicios del sistema, la aplicación
 * (para cambiar de ventana) y acciones comunes como ejecutar con manejo de errores e imprimir.
 */
public class ContextoGui {

    /** Acción de negocio que puede fallar con un mensaje para el usuario. */
    @FunctionalInterface
    public interface Accion {
        void ejecutar() throws CocheraException;
    }

    private final ContextoAplicacion app;
    private final AplicacionGrafica aplicacion;
    private Runnable oyenteCambios;

    public ContextoGui(ContextoAplicacion app, AplicacionGrafica aplicacion) {
        this.app = app;
        this.aplicacion = aplicacion;
    }

    public ContextoAplicacion getApp() {
        return app;
    }

    public AplicacionGrafica getAplicacion() {
        return aplicacion;
    }

    /** La ventana principal se registra aquí para actualizar su encabezado cuando algo cambia. */
    public void setOyenteCambios(Runnable oyente) {
        this.oyenteCambios = oyente;
    }

    public void notificarCambio() {
        if (oyenteCambios != null) {
            oyenteCambios.run();
        }
    }

    /** Ejecuta la acción; si falla una regla de negocio muestra el mensaje. Devuelve true si salió bien. */
    public boolean ejecutar(Component padre, Accion accion) {
        try {
            accion.ejecutar();
            return true;
        } catch (CocheraException e) {
            Dialogos.error(padre, e.getMessage());
            return false;
        } finally {
            notificarCambio();
        }
    }

    /**
     * Imprime un ticket. Si la impresora no está conectada, el registro ya se guardó:
     * se ofrece conectar e imprimir, o reimprimir más tarde sin duplicar nada.
     */
    public void imprimir(Component padre, TicketImprimible documento) {
        Impresora impresora = app.getImpresora();
        if (!impresora.estaConectada()) {
            if (!Dialogos.confirmar(padre, "La impresora no está conectada.\n"
                    + "El registro ya se guardó. ¿Conectar la impresora e imprimir ahora?")) {
                return;
            }
            try {
                impresora.conectar();
            } catch (CocheraException e) {
                Dialogos.error(padre, e.getMessage());
                return;
            }
        }
        try {
            impresora.imprimir(documento.generar());
        } catch (CocheraException e) {
            Dialogos.error(padre, "No se pudo imprimir: " + e.getMessage() + "\nPuedes reimprimir luego.");
        }
        notificarCambio();
    }
}
