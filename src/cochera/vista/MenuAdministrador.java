package cochera.vista;

import cochera.ContextoAplicacion;
import cochera.dto.CierreGeneral;
import cochera.dto.FilaExport;
import cochera.dto.KpiHoy;
import cochera.dto.MontoPorTipo;
import cochera.dto.PaginaResultado;
import cochera.dto.ReporteTurno;
import cochera.excepcion.CocheraException;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.Tarifa;
import cochera.modelo.TurnoCaja;
import cochera.modelo.Usuario;
import cochera.servicio.AdminServicio;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import cochera.util.Texto;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Menú del ADMINISTRADOR: KPIs, cierre 2 PM, auditoría, pagos, tarifario, exportación y usuarios.
 */
public class MenuAdministrador extends MenuBase {

    /** Obtiene una página de resultados (puede fallar por permisos). */
    @FunctionalInterface
    private interface Paginador<T> {
        PaginaResultado<T> obtener(int pagina) throws CocheraException;
    }

    /** Qué hacer cuando el usuario elige un elemento de la lista (null = solo lectura). */
    @FunctionalInterface
    private interface AlElegir<T> {
        void elegir(T elemento) throws CocheraException;
    }

    public MenuAdministrador(ContextoAplicacion ctx, Consola consola) {
        super(ctx, consola);
    }

    @Override
    protected String getTitulo() {
        return "Cochera Los Angeles - Panel de administracion";
    }

    @Override
    protected void mostrarEncabezado() {
        consola.info(">> Vehiculos dentro: " + ctx.getBaseDatos().listarTicketsEnCurso().size());
    }

    @Override
    protected List<String> getOpciones() {
        return Arrays.asList(
                "Resumen del dia (KPIs)",
                "Cierre general 2 PM (corte de 24 h)",
                "Auditoria de turnos cerrados",
                "Pagos registrados",
                "Tarifario",
                "Exportar reporte de ventas (Excel / CSV)",
                "Autos dentro",
                "Usuarios");
    }

    @Override
    protected void atenderOpcion(int opcion) throws CocheraException {
        switch (opcion) {
            case 1:
                verResumenDelDia();
                break;
            case 2:
                verCierreGeneral();
                break;
            case 3:
                verTurnosCerrados();
                break;
            case 4:
                verPagos();
                break;
            case 5:
                gestionarTarifario();
                break;
            case 6:
                exportarVentas();
                break;
            case 7:
                verAutosDentro();
                break;
            case 8:
                gestionarUsuarios();
                break;
            default:
                break;
        }
    }

    // ─────────────────────── 1. Resumen del día ───────────────────────

    private void verResumenDelDia() throws CocheraException {
        KpiHoy kpi = ctx.getAdmin().obtenerKpiHoy();
        consola.subtitulo("RESUMEN DE HOY " + FechaUtil.fecha(ctx.getReloj().ahora()));
        consola.info("  Autos dentro: " + kpi.getAutosDentro() + "   Ingresos de hoy: " + Dinero.soles(kpi.getIngresosHoy())
                + "   Tickets cobrados hoy: " + kpi.getTicketsHoy());
        StringBuilder metodos = new StringBuilder("  Por metodo -> ");
        for (MetodoPago m : MetodoPago.values()) {
            metodos.append(m).append(": ").append(Dinero.soles(kpi.getResumen().getPorMetodo(m))).append("   ");
        }
        consola.info(metodos.toString());

        List<TurnoCaja> abiertos = ctx.getAdmin().obtenerTurnosAbiertos();
        consola.info("  Trabajadores en turno: " + (abiertos.isEmpty() ? "ninguno" : ""));
        for (TurnoCaja t : abiertos) {
            consola.info("     - " + t.getEmpleado().getNombreCompleto() + " desde " + FechaUtil.fechaHora(t.getFechaApertura()));
        }

        consola.info("  Ingresos por hora:");
        double[] porHora = kpi.getIngresosPorHora();
        double maximo = 0;
        for (double v : porHora) {
            maximo = Math.max(maximo, v);
        }
        boolean hubo = false;
        for (int h = 0; h < 24; h++) {
            if (porHora[h] > 0) {
                hubo = true;
                int barras = (int) Math.round(porHora[h] / maximo * 30);
                consola.info("     " + (h < 10 ? "0" : "") + h + ":00 " + Texto.repetir('#', Math.max(1, barras))
                        + " " + Dinero.soles(porHora[h]));
            }
        }
        if (!hubo) {
            consola.info("     (sin cobros hoy)");
        }
        consola.info("  Recaudacion por tipo de vehiculo:");
        for (MontoPorTipo m : kpi.getPorTipo()) {
            consola.info("     " + Texto.columna(m.getTipoVehiculo(), 28) + Dinero.soles(m.getMonto()));
        }
        consola.subtitulo("ULTIMOS COBROS DE HOY");
        mostrarTablaPagos(kpi.getRegistrosRecientes(), 1);
        consola.pausa();
    }

    // ─────────────────────── 2. Cierre general 2 PM ───────────────────────

    private void verCierreGeneral() throws CocheraException {
        LocalDate fecha = consola.leerFechaOpcional("Fecha de corte (ENTER = hoy): ");
        CierreGeneral c = ctx.getAdmin().obtenerCierreGeneral(fecha);
        consola.subtitulo("CIERRE GENERAL 2 PM - CORTE " + FechaUtil.fechaIso(c.getFechaCorte()));
        consola.info("  Periodo: " + FechaUtil.fechaHora(c.getDesde()) + " a " + FechaUtil.fechaHora(c.getHasta()) + ":59");
        consola.info("  Recaudacion total: " + Dinero.soles(c.getResumen().getTotal()) + "   Pagos: " + c.getResumen().getCantidad()
                + "   Ticket promedio: " + Dinero.soles(c.getTicketPromedio()));
        consola.info("  Efectivo: " + Dinero.soles(c.getResumen().getEfectivo()) + "   Yape: " + Dinero.soles(c.getResumen().getYape())
                + "   Plin: " + Dinero.soles(c.getResumen().getPlin()));
        consola.info("  Turnos del corte:");
        if (c.getTurnos().isEmpty()) {
            consola.info("     (ninguno)");
        }
        for (ReporteTurno r : c.getTurnos()) {
            consola.info("     " + filaTurno(r) + "  En corte: " + Dinero.soles(r.getRecaudado()));
        }
        if (!c.getPagos().isEmpty() && consola.confirmar("Ver los " + c.getPagos().size() + " pagos del corte?")) {
            mostrarTablaPagos(c.getPagos(), 1);
        }
        if (consola.confirmar("Exportar este cierre a CSV (Excel)?")) {
            try {
                File archivo = ctx.getExportador().exportarCierre(c);
                consola.exito("Archivo generado: " + archivo.getAbsolutePath());
            } catch (IOException e) {
                consola.error("No se pudo exportar: " + e.getMessage());
            }
        }
    }

    // ─────────────────────── 3. Auditoría de turnos ───────────────────────

    private void verTurnosCerrados() throws CocheraException {
        final LocalDate desde = consola.leerFechaOpcional("Desde (fecha de cierre, ENTER = sin limite): ");
        final LocalDate hasta = consola.leerFechaOpcional("Hasta (ENTER = sin limite): ");
        navegar("TURNOS CERRADOS",
                pagina -> ctx.getAdmin().obtenerTurnosCerrados(desde, hasta, pagina),
                (numero, r) -> consola.info(Texto.izquierda(String.valueOf(numero), 2) + ". " + filaTurno(r)
                        + "  Total turno: " + Dinero.soles(r.getRecaudado())),
                null);
    }

    private static String filaTurno(ReporteTurno r) {
        TurnoCaja t = r.getTurno();
        return Texto.columna(t.getEmpleado().getNombreCompleto(), 16) + " "
                + FechaUtil.fechaHora(t.getFechaApertura()) + " -> "
                + Texto.derecha(t.getFechaCierre() == null ? "ABIERTO" : FechaUtil.fechaHora(t.getFechaCierre()), 16)
                + "  Declarado: " + Texto.izquierda(t.getEfectivoDeclarado() == null ? "-" : Dinero.soles(t.getEfectivoDeclarado()), 9)
                + "  Dif.: " + Texto.izquierda(t.getDiferenciaCaja() == null ? "-" : Dinero.conSigno(t.getDiferenciaCaja()), 10);
    }

    // ─────────────────────── 4. Pagos ───────────────────────

    private void verPagos() throws CocheraException {
        final LocalDate desde = consola.leerFechaOpcional("Desde (ENTER = sin limite): ");
        final LocalDate hasta = consola.leerFechaOpcional("Hasta (ENTER = sin limite): ");
        navegarPagos(desde, hasta);
    }

    private void navegarPagos(final LocalDate desde, final LocalDate hasta) throws CocheraException {
        int pagina = 1;
        while (true) {
            PaginaResultado<Pago> p = ctx.getAdmin().obtenerPagos(desde, hasta, pagina);
            consola.subtitulo("PAGOS - pagina " + p.getPagina() + " de " + Math.max(1, p.getPaginas()) + " (" + p.getTotal() + " en total)");
            int inicio = (p.getPagina() - 1) * AdminServicio.TAMANIO_PAGINA + 1;
            mostrarTablaPagos(p.getDatos(), inicio);
            String op = consola.leerTexto("[#] gestionar pago  [S] siguiente  [A] anterior  [0] volver: ").toUpperCase();
            if (op.equals("0") || op.isEmpty()) {
                return;
            } else if (op.equals("S") || op.equals("A")) {
                pagina = cambiarPagina(op, p);
            } else {
                int indice = parsearIndice(op, inicio + p.getDatos().size() - 1) - (inicio - 1);
                if (indice >= 0 && indice < p.getDatos().size()) {
                    gestionarPago(p.getDatos().get(indice));
                } else {
                    consola.error("Opcion no valida.");
                }
            }
        }
    }

    /** El administrador además puede ANULAR el ticket (polimorfismo: amplía el menú del pago). */
    @Override
    protected List<String> opcionesPago(Pago pago) {
        List<String> opciones = super.opcionesPago(pago);
        opciones.add("Anular ticket");
        return opciones;
    }

    @Override
    protected void atenderOpcionPago(Pago pago, int opcion) throws CocheraException {
        if (opcion == 3) {
            if (consola.confirmar("Anular el ticket " + pago.getTicket().getCodigoBarras() + "? El pago queda como historico")) {
                ctx.getAdmin().anularTicket(pago.getTicket());
                consola.exito("Ticket anulado.");
            }
            return;
        }
        super.atenderOpcionPago(pago, opcion);
    }

    // ─────────────────────── 5. Tarifario ───────────────────────

    private void gestionarTarifario() throws CocheraException {
        while (true) {
            List<Tarifa> tarifas = ctx.getAdmin().obtenerTarifario();
            consola.subtitulo("TARIFARIO (tarifa base por bloque de 12 h + sobrecargo por hora extra)");
            for (Tarifa t : tarifas) {
                consola.info("  " + Texto.izquierda(String.valueOf(t.getId()), 2) + ". " + Texto.columna(t.getTipoVehiculo(), 30)
                        + " Base: " + Texto.izquierda(Dinero.soles(t.getCostoBase()), 9)
                        + "   Hora extra: " + Dinero.soles(t.getSobrecargoHora()));
            }
            int id = consola.leerEntero("ID de la tarifa a editar (0 = volver): ", 0, tarifas.get(tarifas.size() - 1).getId());
            if (id == 0) {
                return;
            }
            Tarifa t = ctx.getBaseDatos().buscarTarifa(id);
            Double base = consola.leerDecimalOpcional("Nueva tarifa base (ENTER = " + Dinero.decimal(t.getCostoBase()) + "): S/ ");
            Double extra = consola.leerDecimalOpcional("Nuevo sobrecargo por hora (ENTER = " + Dinero.decimal(t.getSobrecargoHora()) + "): S/ ");
            final double nuevaBase = base == null ? t.getCostoBase() : base;
            final double nuevoExtra = extra == null ? t.getSobrecargoHora() : extra;
            ejecutar(() -> {
                Tarifa actualizada = ctx.getAdmin().actualizarTarifa(id, nuevaBase, nuevoExtra);
                consola.exito("Tarifa actualizada: " + actualizada);
            });
        }
    }

    // ─────────────────────── 6. Exportación ───────────────────────

    private void exportarVentas() throws CocheraException {
        consola.subtitulo("EXPORTAR REPORTE DE VENTAS");
        LocalDate desde = consola.leerFechaOpcional("Desde (ENTER = sin limite): ");
        LocalDate hasta = consola.leerFechaOpcional("Hasta (ENTER = sin limite): ");
        Tarifa tipo = elegirTarifa(ctx.getAdmin().obtenerTarifario(), true);
        List<String> empleados = ctx.getAdmin().obtenerNombresEmpleados();
        for (int i = 0; i < empleados.size(); i++) {
            consola.info("  " + (i + 1) + ". " + empleados.get(i));
        }
        int e = consola.leerEntero("Empleado (0 = todos): ", 0, empleados.size());

        List<FilaExport> filas = ctx.getAdmin().obtenerFilasExport(desde, hasta,
                tipo == null ? null : tipo.getTipoVehiculo(), e == 0 ? null : empleados.get(e - 1));
        if (filas.isEmpty()) {
            consola.aviso("No hay ventas con esos filtros.");
            return;
        }
        try {
            File archivo = ctx.getExportador().exportarVentas(filas);
            double total = 0;
            for (FilaExport f : filas) {
                total += f.getTotalCobrado();
            }
            consola.exito(filas.size() + " ventas exportadas (" + Dinero.soles(total) + ") en: " + archivo.getAbsolutePath());
        } catch (IOException ex) {
            consola.error("No se pudo exportar: " + ex.getMessage());
        }
    }

    // ─────────────────────── 8. Usuarios ───────────────────────

    private void gestionarUsuarios() throws CocheraException {
        consola.subtitulo("USUARIOS");
        for (Usuario u : ctx.getAuth().listarUsuarios()) {
            consola.info("  " + Texto.columna(u.getNombreCompleto(), 24) + Texto.derecha(u.getEmail(), 26) + u.getRol());
        }
        if (consola.confirmar("Registrar un nuevo empleado (cajero)?")) {
            String email = consola.leerObligatorio("Correo: ");
            String nombre = consola.leerObligatorio("Nombre completo: ");
            String clave = consola.leerClave("Contrasena (min. 6 caracteres): ");
            Usuario nuevo = ctx.getAuth().registrarEmpleado(email, clave, nombre);
            consola.exito("Empleado registrado: " + nuevo);
        }
    }

    // ─────────────────────── Navegación paginada GENÉRICA ───────────────────────

    /** Recorre páginas de cualquier tipo T. Fila<T> dibuja cada elemento. */
    private <T> void navegar(String titulo, Paginador<T> paginador, FilaListado<T> fila, AlElegir<T> alElegir)
            throws CocheraException {
        int pagina = 1;
        while (true) {
            PaginaResultado<T> p = paginador.obtener(pagina);
            consola.subtitulo(titulo + " - pagina " + p.getPagina() + " de " + Math.max(1, p.getPaginas()) + " (" + p.getTotal() + " en total)");
            if (p.getDatos().isEmpty()) {
                consola.info("  No hay registros.");
            }
            int inicio = (p.getPagina() - 1) * AdminServicio.TAMANIO_PAGINA + 1;
            for (int i = 0; i < p.getDatos().size(); i++) {
                fila.dibujar(inicio + i, p.getDatos().get(i));
            }
            String op = consola.leerTexto((alElegir != null ? "[#] elegir  " : "") + "[S] siguiente  [A] anterior  [0] volver: ").toUpperCase();
            if (op.equals("0") || op.isEmpty()) {
                return;
            } else if (op.equals("S") || op.equals("A")) {
                pagina = cambiarPagina(op, p);
            } else if (alElegir != null) {
                int indice = parsearIndice(op, inicio + p.getDatos().size() - 1) - (inicio - 1);
                if (indice >= 0 && indice < p.getDatos().size()) {
                    alElegir.elegir(p.getDatos().get(indice));
                }
            }
        }
    }

    /** S = siguiente, A = anterior; avisa si no hay más páginas en esa dirección. */
    private int cambiarPagina(String op, PaginaResultado<?> p) {
        if (op.equals("S") && p.haySiguiente()) {
            return p.getPagina() + 1;
        }
        if (op.equals("A") && p.hayAnterior()) {
            return p.getPagina() - 1;
        }
        consola.aviso(op.equals("S") ? "Ya estas en la ultima pagina." : "Ya estas en la primera pagina.");
        return p.getPagina();
    }

    @FunctionalInterface
    private interface FilaListado<T> {
        void dibujar(int numero, T elemento);
    }
}
