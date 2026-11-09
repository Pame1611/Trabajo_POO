package cochera;

/**
 * Clase ejecutable de la versión de CONSOLA (menús de texto).
 * Usa exactamente las mismas clases de negocio que la versión con formularios.
 */
public class Ejec_CocheraConsola {

    public static void main(String[] args) {
        SistemaCochera sistema = new SistemaCochera();
        sistema.iniciar();
    }
}
