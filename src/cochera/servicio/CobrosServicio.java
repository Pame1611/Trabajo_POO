package cochera.servicio;

import cochera.datos.BaseDatos;
import cochera.dto.KpiTurno;
import cochera.dto.ResumenPagos;
import cochera.excepcion.AccesoDenegadoException;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.TicketCochera;
import cochera.modelo.TurnoCaja;
import cochera.modelo.Usuario;
import cochera.util.Placa;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Pantalla "Mis cobros" del cajero (app/actions/cobros.ts): KPIs del turno,
 * lista de pagos, tickets atendidos y corrección del método de pago.
 */
public class CobrosServicio {

    private final BaseDatos bd;
    private final AutenticacionServicio auth;
    private final TurnoServicio turnos;

    public CobrosServicio(BaseDatos bd, AutenticacionServicio auth, TurnoServicio turnos) {
        this.bd = bd;
        this.auth = auth;
        this.turnos = turnos;
    }

    private TurnoCaja requerirTurnoDelEmpleado() throws CocheraException {
        auth.requerirEmpleado("Esta sección es solo para empleados.");
        return turnos.requerirTurnoAbierto("Debes abrir un turno para ver tus cobros.");
    }

    public KpiTurno obtenerKpiTurnoActual() throws CocheraException {
        TurnoCaja turno = requerirTurnoDelEmpleado();
        return new KpiTurno(turno, ResumenPagos.de(bd.pagosDeTurno(turno)), bd.listarTicketsEnCurso().size());
    }

    /** Pagos del turno actual, del más reciente al más antiguo. */
    public List<Pago> obtenerPagosTurno() throws CocheraException {
        TurnoCaja turno = requerirTurnoDelEmpleado();
        List<Pago> lista = bd.pagosDeTurno(turno);
        lista.sort(Comparator.comparing(Pago::getCreadoEn).reversed());
        return lista;
    }

    /** Tickets cuyo cobro pertenece a este turno. */
    public List<TicketCochera> obtenerTicketsTurno() throws CocheraException {
        List<TicketCochera> tickets = new ArrayList<>();
        for (Pago p : obtenerPagosTurno()) {
            tickets.add(p.getTicket());
        }
        tickets.sort(Comparator.comparing(TicketCochera::getHoraEntrada).reversed());
        return tickets;
    }

    public static List<Pago> filtrarPorPlaca(List<Pago> pagos, String filtro) {
        String f = Placa.normalizar(filtro);
        if (f.isEmpty()) {
            return pagos;
        }
        List<Pago> filtrados = new ArrayList<>();
        for (Pago p : pagos) {
            if (p.getTicket().getPlaca().contains(f)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    /**
     * Corrige el método de pago. Permitido al ADMIN, o al cajero dueño del turno
     * mientras ese turno siga ABIERTO (política RLS "actualizar pago").
     */
    public void cambiarMetodoPago(Pago pago, MetodoPago nuevo) throws CocheraException {
        Usuario u = auth.requerirSesion();
        boolean permitido = u.esAdministrador()
                || (pago.getTurno().estaAbierto() && pago.getTurno().perteneceA(u));
        if (!permitido) {
            throw new AccesoDenegadoException("No se pudo cambiar el método de pago. "
                    + "Asegúrate de tener permisos o que el turno siga abierto.");
        }
        if (nuevo == pago.getMetodoPago()) {
            throw new ValidacionException("El pago ya está registrado como " + nuevo + ".");
        }
        pago.cambiarMetodoPago(nuevo);
    }
}
