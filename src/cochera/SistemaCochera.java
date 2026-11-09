package cochera;

import cochera.excepcion.CocheraException;
import cochera.modelo.Usuario;
import cochera.vista.Consola;
import cochera.vista.FinDeEntradaException;
import cochera.vista.MenuAdministrador;
import cochera.vista.MenuBase;
import cochera.vista.MenuDemostracion;
import cochera.vista.MenuEmpleado;

/**
 * Punto de arranque de la aplicación de consola: pantalla de inicio y login.
 * Según el rol del usuario abre el menú correspondiente (POLIMORFISMO: la variable
 * es de tipo MenuBase, pero el objeto real es MenuEmpleado o MenuAdministrador).
 */
public class SistemaCochera {

    /** Carga un turno de ayer y 4 autos dentro para la exposición. */
    public static final boolean CARGAR_DATOS_DEMOSTRACION = true;

    private final ContextoAplicacion ctx;
    private final Consola consola;

    public SistemaCochera() {
        this.ctx = new ContextoAplicacion(CARGAR_DATOS_DEMOSTRACION);
        this.consola = new Consola();
    }

    public void iniciar() {
        try {
            while (true) {
                consola.titulo("Cochera Los Angeles - Sistema de gestion");
                consola.info("  Representaciones e Inversiones Los Angeles SAC - La Esperanza, Trujillo");
                consola.linea();
                consola.info("  1. Iniciar sesion");
                consola.info("  2. Herramientas de demostracion (reloj, impresora, usuarios de prueba)");
                consola.info("  0. Salir");
                int opcion = consola.leerEntero("Opcion: ", 0, 2);
                if (opcion == 0) {
                    consola.info("Hasta luego.");
                    return;
                }
                if (opcion == 1) {
                    iniciarSesion();
                } else {
                    new MenuDemostracion(ctx, consola).mostrar();
                }
            }
        } catch (FinDeEntradaException e) {
            consola.info("");
            consola.info("Entrada finalizada. Hasta luego.");
        }
    }

    private void iniciarSesion() {
        consola.titulo("Iniciar sesion");
        String email = consola.leerTexto("Correo: ");
        String clave = consola.leerClave("Contrasena: ");
        try {
            Usuario usuario = ctx.getAuth().iniciarSesion(email, clave);
            consola.exito("Bienvenido(a), " + usuario.getNombreCompleto() + ".");
            MenuBase menu = crearMenuPara(usuario);
            menu.mostrar();
        } catch (CocheraException e) {
            consola.error(e.getMessage());
        }
    }

    private MenuBase crearMenuPara(Usuario usuario) {
        if (usuario.esAdministrador()) {
            return new MenuAdministrador(ctx, consola);
        }
        return new MenuEmpleado(ctx, consola);
    }
}
