package cochera.vista;

import cochera.ContextoAplicacion;
import cochera.dto.CotizacionSalida;
import cochera.dto.KpiTurno;
import cochera.dto.ResultadoCobro;
import cochera.dto.ResumenPagos;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.impresion.TicketEntrada;
import cochera.impresion.TicketSalida;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.modelo.TurnoCaja;
import cochera.servicio.CobrosServicio;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import cochera.util.Placa;
import cochera.util.Texto;
import java.util.ArrayList;
import java.util.List;

/**
 * Menú del CAJERO: turno de caja, check-in, check-out, autos dentro y sus cobros.
 */
public class MenuEmpleado extends MenuBase {

    public MenuEmpleado(ContextoAplicacion ctx, Consola consola) {
        super(ctx, consola);
    }

    @Override
    protected String getTitulo() {
        return "Cochera Los Angeles - Caja";
    }

    /** Equivale al TurnoBadge de la web. */
    @Override
    protected void mostrarEncabezado() {
        TurnoCaja turno = ctx.getTurnos().obtenerTurnoAbierto();
        if (turno == null) {
            consola.info(">> SIN TURNO: abre tu turno de caja para registrar entradas y cobrar.");
        } else {
            long minutos = FechaUtil.minutosEntre(turno.getFechaApertura(), ctx.getReloj().ahora());
            consola.info(">> TURNO ABIERTO desde " + FechaUtil.fechaHora(turno.getFechaApertura())
                    + " (" + FechaUtil.formatoMinutos(minutos) + ")");
        }
    }

    @Override
    protected List<String> getOpciones() {
        List<String> op = new ArrayList<>();
        op.add(ctx.getTurnos().obtenerTurnoAbierto() == null ? "Abrir turno de caja" : "Cerrar turno de caja (arqueo)");
        op.add("Check-in: registrar entrada");
        op.add("Check-out: cobrar salida");
        op.add("Autos dentro");
        op.add("Mis cobros del turno");
        op.add(ctx.getImpresora().estaConectada() ? "Desconectar impresora" : "Conectar impresora");
        return op;
    }

    @Override
    protected void atenderOpcion(int opcion) throws CocheraException {
        switch (opcion) {
            case 1:
                if (ctx.getTurnos().obtenerTurnoAbierto() == null) {
                    abrirTurno();
                } else {
                    cerrarTurno();
                }
                break;
            case 2:
                registrarEntrada();
                break;
            case 3:
                cobrarSalida();
                break;
            case 4:
                verAutosDentro();
                break;
            case 5:
                verMisCobros();
                break;
            case 6:
                alternarConexionImpresora();
                break;
            default:
                break;
        }
    }

    // ─────────────────────── Turno ───────────────────────

    private void abrirTurno() throws CocheraException {
        TurnoCaja turno = ctx.getTurnos().abrirTurno();
        consola.exito("Turno abierto a las " + FechaUtil.hora(turno.getFechaApertura()) + ". Ya puedes operar.");
    }

    /** Resumen y arqueo (CerrarTurnoCard). */
    private void cerrarTurno() throws CocheraException {
        TurnoCaja turno = ctx.getTurnos().requerirTurnoAbierto("No tienes un turno abierto.");
        ResumenPagos r = ctx.getTurnos().obtenerResumenPagos(turno);
        consola.subtitulo("CIERRE DE TURNO - RESUMEN Y ARQUEO");
        consola.info("  Cobros del turno:      " + r.getCantidad());
        consola.info("  Efectivo (sistema):    " + Dinero.soles(r.getEfectivo()));
        consola.info("  Yape:                  " + Dinero.soles(r.getYape()) + "  (informativo)");
        consola.info("  Plin:                  " + Dinero.soles(r.getPlin()) + "  (informativo)");
        consola.info("  TOTAL COBRADO:         " + Dinero.soles(r.getTotal()));
        consola.info("  * Diferencia = tu conteo de efectivo - efectivo del sistema.");

        double declarado;
        while (true) {
            declarado = consola.leerDecimal("Tu conteo fisico de efectivo: S/ ");
            if (declarado >= 0) {
                break;
            }
            consola.error("Monto de efectivo invalido.");
        }
        double diferencia = ctx.getTurnos().previsualizarDiferencia(turno, declarado);
        consola.info("  Diferencia de caja:    " + Dinero.conSigno(diferencia) + "  " + estadoCaja(diferencia));
        if (!consola.confirmar("Confirmas cerrar el turno declarando " + Dinero.soles(declarado) + " en efectivo?")) {
            consola.aviso("Cierre cancelado. El turno sigue abierto.");
            return;
        }
        TurnoCaja cerrado = ctx.getTurnos().cerrarTurno(declarado);
        consola.exito("Turno cerrado a las " + FechaUtil.hora(cerrado.getFechaCierre())
                + ". Diferencia registrada: " + Dinero.conSigno(cerrado.getDiferenciaCaja()) + ".");
    }

    private static String estadoCaja(double diferencia) {
        if (diferencia > 0) {
            return "(SOBRANTE)";
        }
        if (diferencia < 0) {
            return "(FALTANTE)";
        }
        return "(CAJA CUADRADA)";
    }

    // ─────────────────────── Check-in ───────────────────────

    private void registrarEntrada() throws CocheraException {
        ctx.getTurnos().requerirTurnoAbierto(
                "No tienes un turno abierto. Abre tu turno desde el Inicio antes de registrar entradas.");
        consola.subtitulo("CHECK-IN - REGISTRAR ENTRADA");
        String placa = Placa.normalizar(consola.leerObligatorio("Placa (ej. ABC-123): "));
        if (placa.length() < 3) {
            throw new ValidacionException("La placa ingresada es demasiado corta.");
        }
        consola.info("  Placa: " + Placa.formatear(placa));
        Tarifa tarifa = elegirTarifa(ctx.getCheckin().obtenerTarifario(), false);

        TicketCochera ticket = ctx.getCheckin().registrarEntrada(placa, tarifa.getId());
        consola.exito("Entrada registrada: " + ticket.getCodigoBarras() + " | " + ticket.getPlacaFormateada()
                + " | " + tarifa.getTipoVehiculo() + " | " + FechaUtil.hora(ticket.getHoraEntrada()));
        imprimir(new TicketEntrada(ticket));
    }

    // ─────────────────────── Check-out ───────────────────────

    private void cobrarSalida() throws CocheraException {
        ctx.getTurnos().requerirTurnoAbierto("No tienes un turno abierto. Abre tu turno desde el Inicio.");
        consola.subtitulo("CHECK-OUT - COBRAR SALIDA");
        String entrada = consola.leerTexto("Escanea el codigo de barras o escribe la placa (ENTER = volver): ");
        if (entrada.isEmpty()) {
            return;
        }
        CotizacionSalida cotizacion;
        try {
            cotizacion = ctx.getCheckout().buscar(entrada);
        } catch (ValidacionException e) {
            consola.error(e.getMessage());
            if (!entrada.toUpperCase().startsWith("TK-")) {
                List<String> sugerencias = ctx.getCheckout().sugerirPlacas(entrada);
                if (!sugerencias.isEmpty()) {
                    consola.info("  Placas dentro que empiezan asi: " + String.join(", ", sugerencias));
                }
            }
            return;
        }
        procesarCobro(cotizacion);
    }

    /** Desde "Autos dentro" se puede cobrar directo sin volver a escribir la placa. */
    @Override
    protected void alSeleccionarVehiculo(TicketCochera ticket) throws CocheraException {
        CotizacionSalida cotizacion = ctx.getCheckout().buscarTicket(ticket.getCodigoBarras(), null);
        mostrarCotizacion(cotizacion.getTicket(), cotizacion.getTarifa());
        if (consola.confirmar("Cobrar la salida de este vehiculo ahora?")) {
            procesarCobro(cotizacion);
        }
    }

    private void procesarCobro(CotizacionSalida cotizacion) throws CocheraException {
        mostrarCotizacion(cotizacion.getTicket(), cotizacion.getTarifa());
        if (!consola.confirmar("Confirmar tarifa y continuar al cobro?")) {
            return;
        }
        double total = cotizacion.getTarifa().getTarifaTotal();
        MetodoPago metodo = elegirMetodoPago();

        Double recibido = null;
        if (metodo == MetodoPago.EFECTIVO) {
            while (true) {
                recibido = consola.leerDecimalOpcional("Monto recibido (ENTER = monto exacto " + Dinero.soles(total) + "): S/ ");
                if (recibido == null) {
                    break;
                }
                try {
                    consola.info("  Vuelto a entregar: " + Dinero.soles(ctx.getCheckout().calcularVuelto(total, recibido)));
                    break;
                } catch (ValidacionException e) {
                    consola.error(e.getMessage());
                }
            }
        }
        if (!consola.confirmar("Estas seguro de haber recibido el pago?")) {
            consola.aviso("Cobro cancelado. El vehiculo sigue EN_CURSO.");
            return;
        }

        ResultadoCobro resultado = ctx.getCheckout().registrarPago(cotizacion.getTicket(), metodo, recibido);
        Pago pago = resultado.getPago();
        if (resultado.getTarifa().getTarifaTotal() != total) {
            consola.aviso("La tarifa se recalculo al momento exacto del cobro: " + Dinero.soles(pago.getMontoTotal()));
        }
        consola.exito("Cobro registrado: " + Dinero.soles(pago.getMontoTotal()) + " (" + pago.getMetodoPago() + ") - "
                + pago.getTicket().getPlacaFormateada() + " puede salir.");
        if (metodo == MetodoPago.EFECTIVO && recibido != null) {
            consola.info("  Recibido: " + Dinero.soles(resultado.getMontoRecibido())
                    + "  |  Vuelto: " + Dinero.soles(resultado.getVuelto()));
        }
        imprimir(new TicketSalida(pago));
    }

    // ─────────────────────── Mis cobros ───────────────────────

    private void verMisCobros() throws CocheraException {
        while (true) {
            KpiTurno kpi = ctx.getCobros().obtenerKpiTurnoActual();
            ResumenPagos r = kpi.getResumen();
            consola.subtitulo("MIS COBROS DEL TURNO");
            consola.info("  Total cobrado: " + Dinero.soles(r.getTotal()) + "   Efectivo: " + Dinero.soles(r.getEfectivo())
                    + "   Yape: " + Dinero.soles(r.getYape()) + "   Plin: " + Dinero.soles(r.getPlin()));
            consola.info("  Tickets cobrados: " + kpi.getTicketsPagados() + "   Vehiculos dentro (cochera): " + kpi.getTicketsEnCurso());
            consola.info("  1. Ver mis pagos (reimprimir / corregir metodo)");
            consola.info("  2. Ver tickets atendidos");
            consola.info("  3. Consultar cuanto paga un vehiculo (por placa)");
            consola.info("  0. Volver");
            int op = consola.leerEntero("Opcion: ", 0, 3);
            if (op == 0) {
                return;
            }
            if (op == 1) {
                ejecutar(this::listarMisPagos);
            } else if (op == 2) {
                ejecutar(this::listarTicketsTurno);
            } else {
                ejecutar(this::consultarVehiculo);
            }
        }
    }

    private void listarMisPagos() throws CocheraException {
        List<Pago> pagos = ctx.getCobros().obtenerPagosTurno();
        String filtro = consola.leerTexto("Filtrar por placa (ENTER = todos): ");
        List<Pago> filtrados = CobrosServicio.filtrarPorPlaca(pagos, filtro);
        mostrarTablaPagos(filtrados, 1);
        if (filtrados.isEmpty()) {
            return;
        }
        int n = consola.leerEntero("Numero de pago para ver acciones (0 = volver): ", 0, filtrados.size());
        if (n > 0) {
            gestionarPago(filtrados.get(n - 1));
        }
    }

    private void listarTicketsTurno() throws CocheraException {
        List<TicketCochera> tickets = ctx.getCobros().obtenerTicketsTurno();
        if (tickets.isEmpty()) {
            consola.info("  Aun no has cobrado tickets en este turno.");
            return;
        }
        for (TicketCochera t : tickets) {
            Pago p = ctx.getBaseDatos().buscarPagoDeTicket(t);
            consola.info("  " + Texto.derecha(t.getCodigoBarras(), 16) + Texto.derecha(t.getPlacaFormateada(), 9)
                    + Texto.columna(t.getTarifa().getTipoVehiculo(), 21) + " "
                    + Texto.derecha(FechaUtil.fechaHora(t.getHoraEntrada()), 18)
                    + Texto.derecha(t.getEstado().name(), 10)
                    + (p == null ? "" : Dinero.soles(p.getMontoTotal())));
        }
    }

    private void consultarVehiculo() throws CocheraException {
        String placa = consola.leerObligatorio("Placa: ");
        CotizacionSalida c = ctx.getCheckout().buscarTicket(null, placa);
        mostrarCotizacion(c.getTicket(), c.getTarifa());
    }
}
