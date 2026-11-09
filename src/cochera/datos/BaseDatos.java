package cochera.datos;

import cochera.excepcion.ValidacionException;
import cochera.modelo.Pago;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.modelo.TurnoCaja;
import cochera.modelo.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base de datos EN MEMORIA. Reemplaza a Supabase/PostgreSQL en la versión de consola.
 *
 * Cada lista equivale a una tabla de schema.sql (perfiles, tarifario, turnos_caja,
 * tickets_cochera, pagos). Los bloques synchronized sobre {@code candado} cumplen el
 * papel de pg_advisory_xact_lock: garantizan códigos de ticket únicos aunque dos
 * cajeros registren entradas al mismo tiempo. Las listas son CopyOnWriteArrayList para
 * que una consulta nunca falle mientras otro cajero está insertando.
 */
public class BaseDatos {

    private final List<Usuario> usuarios = new CopyOnWriteArrayList<>();
    private final List<Tarifa> tarifas = new CopyOnWriteArrayList<>();
    private final List<TurnoCaja> turnos = new CopyOnWriteArrayList<>();
    private final List<TicketCochera> tickets = new CopyOnWriteArrayList<>();
    private final List<Pago> pagos = new CopyOnWriteArrayList<>();
    private final Object candado = new Object();

    // ───────────────────────────── USUARIOS (perfiles) ─────────────────────────────

    public void insertarUsuario(Usuario usuario) throws ValidacionException {
        if (buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            throw new ValidacionException("Ya existe un usuario con el correo " + usuario.getEmail() + ".");
        }
        usuarios.add(usuario);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        if (email == null) {
            return null;
        }
        String buscado = email.trim().toLowerCase();
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(buscado)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    // ───────────────────────────── TARIFARIO ─────────────────────────────

    public void insertarTarifa(Tarifa tarifa) {
        tarifas.add(tarifa);
        tarifas.sort(Comparator.comparingInt(Tarifa::getId));
    }

    public List<Tarifa> listarTarifas() {
        return Collections.unmodifiableList(tarifas);
    }

    public Tarifa buscarTarifa(int id) {
        for (Tarifa t : tarifas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    // ───────────────────────────── TURNOS_CAJA ─────────────────────────────

    public void insertarTurno(TurnoCaja turno) {
        turnos.add(turno);
    }

    /** Turno ABIERTO del usuario (el más reciente si hubiera más de uno). */
    public TurnoCaja buscarTurnoAbierto(Usuario usuario) {
        TurnoCaja encontrado = null;
        for (TurnoCaja t : turnos) {
            if (t.estaAbierto() && t.perteneceA(usuario)) {
                encontrado = t;
            }
        }
        return encontrado;
    }

    public List<TurnoCaja> listarTurnos() {
        return Collections.unmodifiableList(turnos);
    }

    // ───────────────────────────── TICKETS_COCHERA ─────────────────────────────

    /**
     * Respeta la restricción UNIQUE de codigo_barras y vuelve a comprobar, dentro del
     * candado, que la placa no haya entrado un instante antes por otro cajero.
     */
    public void insertarTicket(TicketCochera ticket) throws ValidacionException {
        synchronized (candado) {
            if (buscarTicketPorCodigo(ticket.getCodigoBarras()) != null) {
                throw new ValidacionException("Colisión de correlativo: " + ticket.getCodigoBarras());
            }
            if (ticket.estaEnCurso() && buscarTicketEnCursoPorPlaca(ticket.getPlaca()) != null) {
                throw new ValidacionException("Placa duplicada EN_CURSO: " + ticket.getPlaca());
            }
            tickets.add(ticket);
        }
    }

    /**
     * Check-in ATÓMICO: dentro de un solo bloque bajo candado verifica que la placa no esté
     * EN_CURSO, genera el siguiente código "TK-AAAA-NNNNNN" e inserta el ticket.
     * Así dos cajeros nunca obtienen el mismo número ni registran la misma placa dos veces.
     */
    public TicketCochera registrarTicket(String placa, Tarifa tarifa, LocalDateTime horaEntrada)
            throws ValidacionException {
        synchronized (candado) {
            TicketCochera duplicado = buscarTicketEnCursoPorPlaca(placa);
            if (duplicado != null) {
                throw new ValidacionException("Esta placa ya tiene un ticket activo (" + duplicado.getCodigoBarras()
                        + "). Realiza el check-out primero.");
            }
            TicketCochera ticket = new TicketCochera(placa, tarifa, horaEntrada,
                    generarCorrelativoTicket(horaEntrada.getYear()));
            tickets.add(ticket);
            return ticket;
        }
    }

    public TicketCochera buscarTicketPorCodigo(String codigo) {
        for (TicketCochera t : tickets) {
            if (t.getCodigoBarras().equalsIgnoreCase(codigo)) {
                return t;
            }
        }
        return null;
    }

    public TicketCochera buscarTicketEnCursoPorPlaca(String placa) {
        for (TicketCochera t : tickets) {
            if (t.estaEnCurso() && t.getPlaca().equals(placa)) {
                return t;
            }
        }
        return null;
    }

    public List<TicketCochera> listarTicketsEnCurso() {
        List<TicketCochera> enCurso = new ArrayList<>();
        for (TicketCochera t : tickets) {
            if (t.estaEnCurso()) {
                enCurso.add(t);
            }
        }
        return enCurso;
    }

    public List<TicketCochera> listarTickets() {
        return Collections.unmodifiableList(tickets);
    }

    /**
     * Genera el código de barras "TK-2026-000001" (función generar_correlativo()).
     * Lee el MÁXIMO del año bajo candado para que dos check-ins no obtengan el mismo número.
     */
    public String generarCorrelativoTicket(int anio) {
        synchronized (candado) {
            String prefijo = "TK-" + anio + "-";
            int maximo = 0;
            for (TicketCochera t : tickets) {
                if (t.getCodigoBarras().startsWith(prefijo)) {
                    int n = Integer.parseInt(t.getCodigoBarras().substring(prefijo.length()));
                    maximo = Math.max(maximo, n);
                }
            }
            return String.format("%s%06d", prefijo, maximo + 1);
        }
    }

    // ───────────────────────────── PAGOS ─────────────────────────────

    public void insertarPago(Pago pago) {
        synchronized (candado) {
            pagos.add(pago);
        }
    }

    public Pago buscarPagoDeTicket(TicketCochera ticket) {
        for (Pago p : pagos) {
            if (p.getTicket() == ticket) {
                return p;
            }
        }
        return null;
    }

    public List<Pago> listarPagos() {
        return Collections.unmodifiableList(pagos);
    }

    public List<Pago> pagosDeTurno(TurnoCaja turno) {
        List<Pago> lista = new ArrayList<>();
        for (Pago p : pagos) {
            if (p.getTurno() == turno) {
                lista.add(p);
            }
        }
        return lista;
    }
}
