package cochera.vista;

import cochera.ContextoAplicacion;
import cochera.datos.DatosIniciales;
import cochera.impresion.Impresora;
import cochera.modelo.Usuario;
import cochera.util.FechaUtil;
import cochera.util.Texto;

/**
 * Herramientas para la exposición: adelantar el reloj (para ver los cobros por bloques
 * de 12 h sin esperar), cambiar de impresora y ver los usuarios de prueba.
 */
public class MenuDemostracion {

    private final ContextoAplicacion ctx;
    private final Consola consola;

    public MenuDemostracion(ContextoAplicacion ctx, Consola consola) {
        this.ctx = ctx;
        this.consola = consola;
    }

    public void mostrar() {
        while (true) {
            consola.titulo("Herramientas de demostracion");
            long desfase = ctx.getReloj().getMinutosDesfase();
            consola.info("Hora del sistema: " + FechaUtil.fechaHora(ctx.getReloj().ahora())
                    + (desfase > 0 ? "  (adelantado " + FechaUtil.formatoMinutos(desfase) + ")" : "  (hora real de Lima)"));
            consola.info("Impresora activa: " + ctx.getImpresora().getNombre());
            consola.linea();
            consola.info("  1. Adelantar el reloj");
            consola.info("  2. Volver a la hora real");
            consola.info("  3. Cambiar impresora (consola <-> archivos .txt)");
            consola.info("  4. Ver usuarios de prueba");
            consola.info("  0. Volver");
            int op = consola.leerEntero("Opcion: ", 0, 4);
            switch (op) {
                case 0:
                    return;
                case 1:
                    int horas = consola.leerEntero("Horas a adelantar (0-72): ", 0, 72);
                    int minutos = consola.leerEntero("Minutos a adelantar (0-59): ", 0, 59);
                    ctx.getReloj().adelantarMinutos(horas * 60L + minutos);
                    consola.exito("Ahora son las " + FechaUtil.fechaHora(ctx.getReloj().ahora()) + ".");
                    break;
                case 2:
                    ctx.getReloj().reiniciar();
                    consola.exito("Reloj en hora real.");
                    break;
                case 3:
                    Impresora nueva = ctx.alternarImpresora();
                    consola.exito("Impresora activa: " + nueva.getNombre());
                    break;
                case 4:
                    mostrarUsuariosDePrueba();
                    break;
                default:
                    break;
            }
        }
    }

    private void mostrarUsuariosDePrueba() {
        consola.subtitulo("USUARIOS DE PRUEBA");
        for (Usuario u : ctx.getBaseDatos().listarUsuarios()) {
            String clave = u.esAdministrador() ? DatosIniciales.CLAVE_DEMO_ADMIN : DatosIniciales.CLAVE_DEMO_CAJERO;
            consola.info("  " + Texto.derecha(u.getEmail(), 24) + Texto.derecha(clave, 12) + u.getNombreCompleto()
                    + " (" + u.getRol() + ")");
        }
        consola.info("  (Los empleados registrados por el admin usan la clave que se les asigno.)");
    }
}
