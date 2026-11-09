package cochera.servicio;

import cochera.datos.BaseDatos;
import cochera.dto.CierreGeneral;
import cochera.dto.FilaExport;
import cochera.dto.KpiHoy;
import cochera.dto.MontoPorTipo;
import cochera.dto.PaginaResultado;
import cochera.dto.ReporteTurno;
import cochera.dto.ResumenPagos;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.modelo.Empleado;
import cochera.modelo.EstadoTicket;
import cochera.modelo.Pago;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.modelo.TurnoCaja;
import cochera.modelo.Usuario;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import cochera.util.Reloj;
import cochera.util.Texto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel del administrador (app/actions/admin.ts): KPIs, auditoría de turnos,
 * pagos, anulaciones, tarifario, exportación y Cierre General de 2 PM.
 */
public class AdminServicio {

    public static final int TAMANIO_PAGINA = 10;
    public static final LocalTime HORA_CORTE = LocalTime.of(14, 0);

    private final BaseDatos bd;
    private final Reloj reloj;
    private final AutenticacionServicio auth;

    public AdminServicio(BaseDatos bd, Reloj reloj, AutenticacionServicio auth) {
        this.bd = bd;
        this.reloj = reloj;
        this.auth = auth;
    }

    // ─────────────────────────── KPIs del día (hora Lima) ───────────────────────────

    public KpiHoy obtenerKpiHoy() throws CocheraException {
        auth.requerirAdmin();
        LocalDateTime inicio = reloj.ahora().toLocalDate().atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);

        List<Pago> pagosHoy = pagosEntre(inicio, fin.minusSeconds(1));
        double[] porHora = new double[24];
        Map<String, Double> porTipo = new LinkedHashMap<>();
        for (Pago p : pagosHoy) {
            porHora[p.getCreadoEn().getHour()] += p.getMontoTotal();
            String tipo = p.getTicket().getTarifa().getTipoVehiculo();
            Double acumulado = porTipo.get(tipo);
            porTipo.put(tipo, (acumulado == null ? 0 : acumulado) + p.getMontoTotal());
        }
        List<MontoPorTipo> rankingTipos = new ArrayList<>();
        for (Map.Entry<String, Double> e : porTipo.entrySet()) {
            rankingTipos.add(new MontoPorTipo(e.getKey(), Dinero.redondear(e.getValue())));
        }
        rankingTipos.sort(Comparator.comparingDouble(MontoPorTipo::getMonto).reversed());

        List<Pago> recientes = new ArrayList<>(pagosHoy.subList(0, Math.min(10, pagosHoy.size())));
        return new KpiHoy(bd.listarTicketsEnCurso().size(), ResumenPagos.de(pagosHoy), porHora, rankingTipos, recientes);
    }

    /** Trabajadores con turno ABIERTO ahora mismo (uno por empleado). */
    public List<TurnoCaja> obtenerTurnosAbiertos() throws CocheraException {
        auth.requerirAdmin();
        Map<String, TurnoCaja> porEmpleado = new LinkedHashMap<>();
        for (TurnoCaja t : bd.listarTurnos()) {
            if (t.estaAbierto()) {
                porEmpleado.put(t.getEmpleado().getId(), t); // si hubiera 2, queda el más reciente
            }
        }
        List<TurnoCaja> lista = new ArrayList<>(porEmpleado.values());
        lista.sort(Comparator.comparing(TurnoCaja::getFechaApertura));
        return lista;
    }

    // ─────────────────────────── Auditoría de turnos ───────────────────────────

    /** Turnos CERRADOS (por fecha de cierre) con el total cobrado en cada uno. */
    public PaginaResultado<ReporteTurno> obtenerTurnosCerrados(LocalDate desde, LocalDate hasta, int pagina)
            throws CocheraException {
        auth.requerirAdmin();
        List<ReporteTurno> lista = new ArrayList<>();
        for (TurnoCaja t : bd.listarTurnos()) {
            if (!t.estaAbierto() && dentroDeRango(t.getFechaCierre(), desde, hasta)) {
                lista.add(new ReporteTurno(t, ResumenPagos.de(bd.pagosDeTurno(t)).getTotal()));
            }
        }
        lista.sort(new Comparator<ReporteTurno>() {
            @Override
            public int compare(ReporteTurno a, ReporteTurno b) {
                return b.getTurno().getFechaCierre().compareTo(a.getTurno().getFechaCierre());
            }
        });
        return PaginaResultado.paginar(lista, pagina, TAMANIO_PAGINA);
    }

    // ─────────────────────────── Pagos ───────────────────────────

    public PaginaResultado<Pago> obtenerPagos(LocalDate desde, LocalDate hasta, int pagina) throws CocheraException {
        auth.requerirAdmin();
        List<Pago> lista = new ArrayList<>();
        for (Pago p : bd.listarPagos()) {
            if (dentroDeRango(p.getCreadoEn(), desde, hasta)) {
                lista.add(p);
            }
        }
        lista.sort(Comparator.comparing(Pago::getCreadoEn).reversed());
        return PaginaResultado.paginar(lista, pagina, TAMANIO_PAGINA);
    }

    /** Anula el ticket; el pago queda como registro histórico. */
    public void anularTicket(TicketCochera ticket) throws CocheraException {
        auth.requerirAdmin();
        if (ticket == null) {
            throw new ValidacionException("Ticket no encontrado.");
        }
        if (ticket.getEstado() == EstadoTicket.ANULADO) {
            throw new ValidacionException("El ticket ya estaba anulado.");
        }
        ticket.anular();
    }

    // ─────────────────────────── Tarifario ───────────────────────────

    public List<Tarifa> obtenerTarifario() throws CocheraException {
        auth.requerirAdmin();
        return bd.listarTarifas();
    }

    /** Solo cambia precios de una categoría existente; no crea ni elimina categorías. */
    public Tarifa actualizarTarifa(int id, double costoBase, double sobrecargoHora) throws CocheraException {
        auth.requerirAdmin();
        Tarifa tarifa = bd.buscarTarifa(id);
        if (tarifa == null) {
            throw new ValidacionException("Tarifa no encontrada.");
        }
        try {
            tarifa.actualizarPrecios(costoBase, sobrecargoHora);
        } catch (IllegalArgumentException e) {
            throw new ValidacionException(e.getMessage());
        }
        return tarifa;
    }

    // ─────────────────────────── Exportación ───────────────────────────

    /** Filas del reporte de ventas con filtros opcionales (null = sin filtro). */
    public List<FilaExport> obtenerFilasExport(LocalDate desde, LocalDate hasta, String tipoVehiculo, String empleado)
            throws CocheraException {
        auth.requerirAdmin();
        List<Pago> pagos = new ArrayList<>(bd.listarPagos());
        pagos.sort(Comparator.comparing(Pago::getCreadoEn).reversed());

        List<FilaExport> filas = new ArrayList<>();
        for (Pago p : pagos) {
            TicketCochera t = p.getTicket();
            String tipo = t.getTarifa().getTipoVehiculo();
            String nombreEmpleado = p.getTurno().getEmpleado().getNombreCompleto();
            if (!dentroDeRango(p.getCreadoEn(), desde, hasta)
                    || (!Texto.vacio(tipoVehiculo) && !tipo.equals(tipoVehiculo))
                    || (!Texto.vacio(empleado) && !nombreEmpleado.equals(empleado))) {
                continue;
            }
            LocalDateTime salida = t.getHoraSalida() != null ? t.getHoraSalida() : p.getCreadoEn();
            long minutos = Math.max(0, FechaUtil.minutosEntre(t.getHoraEntrada(), salida));
            double base = t.getTarifa().getCostoBase();
            filas.add(new FilaExport(
                    t.getCodigoBarras(),
                    FechaUtil.fecha(p.getCreadoEn()),
                    t.getPlacaFormateada(),
                    tipo,
                    FechaUtil.fechaHora(t.getHoraEntrada()),
                    FechaUtil.fechaHora(salida),
                    (minutos / 60) + "h " + (minutos % 60) + "m",
                    minutos,
                    base,
                    Dinero.redondear(Math.max(0, p.getMontoTotal() - base)),
                    p.getMontoTotal(),
                    p.getMetodoPago().getEtiqueta(),
                    t.getEstado().name(),
                    nombreEmpleado));
        }
        return filas;
    }

    public List<String> obtenerNombresEmpleados() throws CocheraException {
        auth.requerirAdmin();
        List<String> nombres = new ArrayList<>();
        for (Usuario u : bd.listarUsuarios()) {
            if (u instanceof Empleado) {
                nombres.add(u.getNombreCompleto());
            }
        }
        return nombres;
    }

    // ─────────────────────────── Cierre General 2 PM ───────────────────────────

    /**
     * Cierre operativo de 24 h: desde las 14:00:00 del día anterior hasta las
     * 13:59:59 del día de corte. Incluye los turnos abiertos o cerrados en ese
     * rango y los que cobraron algo dentro del corte.
     */
    public CierreGeneral obtenerCierreGeneral(LocalDate fechaCorte) throws CocheraException {
        auth.requerirAdmin();
        LocalDate fecha = fechaCorte != null ? fechaCorte : reloj.ahora().toLocalDate();
        LocalDateTime desde = fecha.minusDays(1).atTime(HORA_CORTE);
        LocalDateTime hasta = fecha.atTime(HORA_CORTE).minusSeconds(1);

        List<Pago> pagos = pagosEntre(desde, hasta);
        Map<TurnoCaja, Double> recaudadoPorTurno = new LinkedHashMap<>();
        for (Pago p : pagos) {
            Double acumulado = recaudadoPorTurno.get(p.getTurno());
            recaudadoPorTurno.put(p.getTurno(), (acumulado == null ? 0 : acumulado) + p.getMontoTotal());
        }

        List<ReporteTurno> turnos = new ArrayList<>();
        for (TurnoCaja t : bd.listarTurnos()) {
            boolean abrioEnCorte = !t.getFechaApertura().isBefore(desde) && !t.getFechaApertura().isAfter(hasta);
            boolean cerroEnCorte = t.getFechaCierre() != null
                    && !t.getFechaCierre().isBefore(desde) && !t.getFechaCierre().isAfter(hasta);
            if (abrioEnCorte || cerroEnCorte || recaudadoPorTurno.containsKey(t)) {
                Double monto = recaudadoPorTurno.get(t);
                turnos.add(new ReporteTurno(t, Dinero.redondear(monto == null ? 0 : monto)));
            }
        }
        turnos.sort(new Comparator<ReporteTurno>() {
            @Override
            public int compare(ReporteTurno a, ReporteTurno b) {
                return a.getTurno().getFechaApertura().compareTo(b.getTurno().getFechaApertura());
            }
        });
        return new CierreGeneral(fecha, desde, hasta, ResumenPagos.de(pagos), turnos, pagos);
    }

    // ─────────────────────────── helpers ───────────────────────────

    /** Pagos con fecha entre desde y hasta (ambos inclusive), del más reciente al más antiguo. */
    private List<Pago> pagosEntre(LocalDateTime desde, LocalDateTime hasta) {
        List<Pago> lista = new ArrayList<>();
        for (Pago p : bd.listarPagos()) {
            if (!p.getCreadoEn().isBefore(desde) && !p.getCreadoEn().isAfter(hasta)) {
                lista.add(p);
            }
        }
        lista.sort(Comparator.comparing(Pago::getCreadoEn).reversed());
        return lista;
    }

    private static boolean dentroDeRango(LocalDateTime fecha, LocalDate desde, LocalDate hasta) {
        if (fecha == null) {
            return false;
        }
        if (desde != null && fecha.isBefore(desde.atStartOfDay())) {
            return false;
        }
        return hasta == null || fecha.isBefore(hasta.plusDays(1).atStartOfDay());
    }
}
