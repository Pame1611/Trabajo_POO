package cochera;

import cochera.gui.AplicacionGrafica;

/**
 * Clase ejecutable (versión con formularios). Solo crea el objeto principal y llama
 * a su método; toda la lógica vive en las clases del sistema.
 */
public class Ejec_Cochera {

    public static void main(String[] args) {
        AplicacionGrafica aplicacion = new AplicacionGrafica();
        aplicacion.iniciar();
    }
}
