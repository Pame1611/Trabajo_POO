package cochera.servicio;

import cochera.datos.BaseDatos;
import cochera.dto.ResumenPagos;
import cochera.excepcion.AccesoDenegadoException;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.modelo.Empleado;
import cochera.modelo.TurnoCaja;
import cochera.modelo.Usuario;
import cochera.util.Dinero;
import cochera.util.Reloj;

/**
 * Apertura, consulta y cierre (arqueo) del turno de caja (app/actions/turno.ts).
 */
public class TurnoServicio {

    private final BaseDatos bd;
    private final Reloj reloj;
    private final AutenticacionServicio auth;

    public TurnoServicio(BaseDatos bd, Reloj reloj, AutenticacionServicio auth) {
        this.bd = bd;
        this.reloj = reloj;
        this.auth = auth;
    }

    /** Solo EMPLEADO y sin otro turno abierto. */
    public TurnoCaja abrirTurno() throws CocheraException {
        Empleado empleado = auth.requerirEmpleado("Solo los empleados pueden abrir un turno de caja.");
        if (bd.buscarTurnoAbierto(empleado) != null) {
            throw new ValidacionException("Ya tienes un turno abierto.");
        }
        TurnoCaja turno = new TurnoCaja(empleado, reloj.ahora());
        bd.insertarTurno(turno);
        return turno;
    }

    /** Turno abierto del usuario en sesión, o null. */
    public TurnoCaja obtenerTurnoAbierto() {
        Usuario u = auth.getUsuarioActual();
        return u == null ? null : bd.buscarTurnoAbierto(u);
    }

    /** Regla central: sin turno abierto no se registra ni se cobra nada. */
    public TurnoCaja requerirTurnoAbierto(String mensajeSiNoHay) throws CocheraException {
        auth.requerirSesion();
        TurnoCaja turno = obtenerTurnoAbierto();
        if (turno == null) {
            throw new AccesoDenegadoException(mensajeSiNoHay);
        }
        return turno;
    }

    public ResumenPagos obtenerResumenPagos(TurnoCaja turno) {
        return ResumenPagos.de(bd.pagosDeTurno(turno));
    }

    /** Vista previa del arqueo antes de confirmar: conteo físico - efectivo del sistema. */
    public double previsualizarDiferencia(TurnoCaja turno, double efectivoDeclarado) {
        return Dinero.redondear(efectivoDeclarado - obtenerResumenPagos(turno).getEfectivo());
    }

    /**
     * Cierra el turno. Solo el EFECTIVO afecta la diferencia de caja;
     * Yape y Plin se muestran pero no son dinero físico que el cajero cuente.
     */
    public TurnoCaja cerrarTurno(double efectivoDeclarado) throws CocheraException {
        auth.requerirSesion();
        if (Double.isNaN(efectivoDeclarado) || efectivoDeclarado < 0) {
            throw new ValidacionException("Monto de efectivo inválido.");
        }
        TurnoCaja turno = requerirTurnoAbierto("No tienes un turno abierto.");
        turno.cerrar(reloj.ahora(), efectivoDeclarado, obtenerResumenPagos(turno).getEfectivo());
        return turno;
    }
}
