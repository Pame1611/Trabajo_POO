package cochera;

import cochera.datos.BaseDatos;
import cochera.datos.DatosIniciales;
import cochera.impresion.Impresora;
import cochera.impresion.ImpresoraArchivo;
import cochera.impresion.ImpresoraTermicaConsola;
import cochera.servicio.AdminServicio;
import cochera.servicio.AutenticacionServicio;
import cochera.servicio.CalculadoraTarifa;
import cochera.servicio.CheckinServicio;
import cochera.servicio.CheckoutServicio;
import cochera.servicio.CobrosServicio;
import cochera.servicio.ExportadorCSV;
import cochera.servicio.MonitoreoServicio;
import cochera.servicio.TurnoServicio;
import cochera.util.Reloj;
import java.io.File;

/**
 * Crea y conecta TODOS los objetos del sistema (inyección de dependencias manual).
 * Cada servicio recibe en su constructor los objetos que necesita.
 */
public class ContextoAplicacion {

    private final Reloj reloj = new Reloj();
    private final BaseDatos baseDatos = new BaseDatos();
    private final CalculadoraTarifa calculadora = new CalculadoraTarifa();
    private final AutenticacionServicio auth;
    private final TurnoServicio turnos;
    private final CheckinServicio checkin;
    private final CheckoutServicio checkout;
    private final CobrosServicio cobros;
    private final MonitoreoServicio monitoreo;
    private final AdminServicio admin;
    private final ExportadorCSV exportador;
    private final Impresora impresoraPrincipal;
    private final Impresora impresoraArchivo = new ImpresoraArchivo(new File("impresiones"));
    private Impresora impresora;

    /** Versión de consola: imprime los tickets en la consola. */
    public ContextoAplicacion(boolean cargarDatosDemostracion) {
        this(cargarDatosDemostracion, new ImpresoraTermicaConsola());
    }

    /** Permite elegir la impresora principal (la versión gráfica usa una ventana). */
    public ContextoAplicacion(boolean cargarDatosDemostracion, Impresora impresoraPrincipal) {
        this.impresoraPrincipal = impresoraPrincipal;
        this.impresora = impresoraPrincipal;
        DatosIniciales.cargar(baseDatos, reloj);
        if (cargarDatosDemostracion) {
            DatosIniciales.cargarDemostracion(baseDatos, reloj);
        }
        auth = new AutenticacionServicio(baseDatos, reloj);
        turnos = new TurnoServicio(baseDatos, reloj, auth);
        checkin = new CheckinServicio(baseDatos, reloj, turnos);
        checkout = new CheckoutServicio(baseDatos, reloj, auth, turnos, calculadora);
        cobros = new CobrosServicio(baseDatos, auth, turnos);
        monitoreo = new MonitoreoServicio(baseDatos, reloj, auth, turnos, calculadora);
        admin = new AdminServicio(baseDatos, reloj, auth);
        exportador = new ExportadorCSV(new File("reportes"));
    }

    /** Alterna entre la impresora principal y la de archivos (polimorfismo vía la interfaz Impresora). */
    public Impresora alternarImpresora() {
        return usarImpresoraArchivo(impresora != impresoraArchivo);
    }

    /** Cambia a la impresora de archivos .txt (true) o a la principal (false), manteniendo la conexión. */
    public Impresora usarImpresoraArchivo(boolean archivo) {
        Impresora nueva = archivo ? impresoraArchivo : impresoraPrincipal;
        if (nueva == impresora) {
            return impresora;
        }
        boolean estabaConectada = impresora.estaConectada();
        impresora.desconectar();
        impresora = nueva;
        if (estabaConectada) {
            try {
                impresora.conectar();
            } catch (Exception e) {
                // queda desconectada; el usuario puede reconectar desde su menú
            }
        }
        return impresora;
    }

    public boolean usaImpresoraArchivo() {
        return impresora == impresoraArchivo;
    }

    public Reloj getReloj() {
        return reloj;
    }

    public BaseDatos getBaseDatos() {
        return baseDatos;
    }

    public CalculadoraTarifa getCalculadora() {
        return calculadora;
    }

    public AutenticacionServicio getAuth() {
        return auth;
    }

    public TurnoServicio getTurnos() {
        return turnos;
    }

    public CheckinServicio getCheckin() {
        return checkin;
    }

    public CheckoutServicio getCheckout() {
        return checkout;
    }

    public CobrosServicio getCobros() {
        return cobros;
    }

    public MonitoreoServicio getMonitoreo() {
        return monitoreo;
    }

    public AdminServicio getAdmin() {
        return admin;
    }

    public ExportadorCSV getExportador() {
        return exportador;
    }

    public Impresora getImpresora() {
        return impresora;
    }
}
