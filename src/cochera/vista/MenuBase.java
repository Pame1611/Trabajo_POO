package cochera.vista;

import cochera.ContextoAplicacion;
import cochera.dto.ResultadoTarifa;
import cochera.excepcion.CocheraException;
import cochera.impresion.Impresora;
import cochera.impresion.TicketImprimible;
import cochera.impresion.TicketSalida;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.modelo.Usuario;
import cochera.servicio.MonitoreoServicio;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import cochera.util.Texto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Menú base (clase ABSTRACTA) del que heredan MenuEmpleado y MenuAdministrador.
 *
 * - mostrar() es un MÉTODO PLANTILLA: el ciclo del menú es igual para todos los roles,
 *   cada subclase solo define su título, sus opciones y qué hace cada opción.
 * - Contiene las pantallas compartidas (autos dentro, pagos, impresión) que ambos heredan.
 */
public abstract class MenuBase {

    protected final ContextoAplicacion ctx;
    protected final Consola consola;

    protected MenuBase(ContextoAplicacion ctx, Consola consola) {
        this.ctx = ctx;
        this.consola = consola;
    }

    /** Acción que puede fallar con una regla de negocio. */
    @FunctionalInterface
    protected interface Accion {
        void ejecutar() throws CocheraException;
    }

    // ─────────────────────── Ciclo del menú (método plantilla) ───────────────────────

    public final void mostrar() {
        while (ctx.getAuth().haySesion()) {
            consola.titulo(getTitulo());
            mostrarEstadoGeneral();
            mostrarEncabezado();
            List<String> opciones = getOpciones();
            consola.linea();
            for (int i = 0; i < opciones.size(); i++) {
                consola.info("  " + (i + 1) + ". " + opciones.get(i));
            }
            consola.info("  0. Cerrar sesion");
            final int opcion = consola.leerEntero("Opcion: ", 0, opciones.size());
            if (opcion == 0) {
                ctx.getAuth().cerrarSesion();
                consola.exito("Sesion cerrada.");
                return;
            }
            ejecutar(() -> atenderOpcion(opcion));
        }
    }

    protected abstract String getTitulo();

    protected abstract void mostrarEncabezado();

    protected abstract List<String> getOpciones();

    protected abstract void atenderOpcion(int opcion) throws CocheraException;

    /** Ejecuta una acción y muestra el mensaje de error de negocio si falla. */
    protected void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (CocheraException e) {
            consola.error(e.getMessage());
        }
    }

    private void mostrarEstadoGeneral() {
        Usuario u = ctx.getAuth().getUsuarioActual();
        long desfase = ctx.getReloj().getMinutosDesfase();
        String reloj = FechaUtil.fechaHora(ctx.getReloj().ahora())
                + (desfase > 0 ? " [reloj adelantado " + FechaUtil.formatoMinutos(desfase) + "]" : "");
        Impresora imp = ctx.getImpresora();
        consola.info("Usuario: " + u + "  |  Hora: " + reloj);
        consola.info("Impresora: " + (imp.estaConectada() ? "CONECTADA" : "desconectada") + " (" + imp.getNombre() + ")");
    }

    // ─────────────────────── Impresión ───────────────────────

    /**
     * Imprime cualquier TicketImprimible (entrada o salida). Si la impresora no está
     * conectada, el registro ya quedó guardado y se ofrece conectar e imprimir sin duplicar nada.
     */
    protected void imprimir(TicketImprimible documento) {
        Impresora imp = ctx.getImpresora();
        if (!imp.estaConectada()) {
            consola.aviso("Impresora no conectada. El registro ya se guardo; puedes imprimir ahora o reimprimir luego.");
            if (!consola.confirmar("Conectar impresora e imprimir " + documento.getTitulo() + "?")) {
                return;
            }
            try {
                imp.conectar();
            } catch (CocheraException e) {
                consola.error(e.getMessage());
                return;
            }
        }
        try {
            imp.imprimir(documento.generar());
        } catch (CocheraException e) {
            consola.error("No se pudo imprimir: " + e.getMessage() + " Puedes reimprimir luego.");
        }
    }

    protected void alternarConexionImpresora() throws CocheraException {
        Impresora imp = ctx.getImpresora();
        if (imp.estaConectada()) {
            imp.desconectar();
            consola.exito("Impresora desconectada.");
        } else {
            imp.conectar();
            consola.exito("Impresora conectada: " + imp.getNombre());
        }
    }

    // ─────────────────────── Selección de datos ───────────────────────

    protected MetodoPago elegirMetodoPago() {
        MetodoPago[] metodos = MetodoPago.values();
        for (int i = 0; i < metodos.length; i++) {
            consola.info("  " + (i + 1) + ". " + metodos[i]);
        }
        return metodos[consola.leerEntero("Metodo de pago: ", 1, metodos.length) - 1];
    }

    /** Muestra el tarifario en dos columnas y devuelve la tarifa elegida (null = todas, si se permite). */
    protected Tarifa elegirTarifa(List<Tarifa> tarifas, boolean permitirTodas) {
        mostrarTarifario(tarifas);
        int max = tarifas.get(tarifas.size() - 1).getId();
        int id = consola.leerEntero("Tipo de vehiculo (" + (permitirTodas ? "0 = todos, " : "") + "1-" + max + "): ",
                permitirTodas ? 0 : 1, max);
        return id == 0 ? null : ctx.getBaseDatos().buscarTarifa(id);
    }

    protected void mostrarTarifario(List<Tarifa> tarifas) {
        int mitad = (tarifas.size() + 1) / 2;
        for (int i = 0; i < mitad; i++) {
            String izquierda = celdaTarifa(tarifas.get(i));
            String derecha = i + mitad < tarifas.size() ? celdaTarifa(tarifas.get(i + mitad)) : "";
            consola.info("  " + Texto.derecha(izquierda, 40) + derecha);
        }
    }

    private static String celdaTarifa(Tarifa t) {
        return Texto.izquierda(String.valueOf(t.getId()), 2) + ". " + Texto.columna(t.getTipoVehiculo(), 24)
                + " S/" + Texto.izquierda(Dinero.decimal(t.getCostoBase()), 5);
    }

    // ─────────────────────── Autos dentro (compartido) ───────────────────────

    protected void verAutosDentro() throws CocheraException {
        MonitoreoServicio mon = ctx.getMonitoreo();
        String filtroPlaca = "";
        Tarifa filtroTipo = null;
        while (true) {
            List<TicketCochera> todos = mon.listarAutosDentro();
            List<TicketCochera> lista = mon.filtrar(todos, filtroPlaca, filtroTipo);
            boolean hayFiltro = !filtroPlaca.isEmpty() || filtroTipo != null;
            consola.subtitulo("AUTOS DENTRO: " + todos.size() + " vehiculo(s) - EN VIVO"
                    + (hayFiltro ? "  | filtro: " + (filtroPlaca.isEmpty() ? "" : "placa '" + filtroPlaca + "' ")
                    + (filtroTipo == null ? "" : "tipo '" + filtroTipo.getTipoVehiculo() + "'") : ""));
            if (lista.isEmpty()) {
                consola.info("  No hay vehiculos que mostrar.");
            } else {
                consola.info(" #   " + Texto.derecha("Codigo", 16) + Texto.derecha("Placa", 9) + Texto.derecha("Tipo", 22)
                        + Texto.derecha("Ingreso", 18) + Texto.derecha("Tiempo", 10) + Texto.derecha("Estimado", 11) + "Alerta");
                for (int i = 0; i < lista.size(); i++) {
                    TicketCochera t = lista.get(i);
                    consola.info(Texto.izquierda(String.valueOf(i + 1), 2) + "   "
                            + Texto.derecha(t.getCodigoBarras(), 16)
                            + Texto.derecha(t.getPlacaFormateada(), 9)
                            + Texto.columna(t.getTarifa().getTipoVehiculo(), 21) + " "
                            + Texto.derecha(FechaUtil.fechaHora(t.getHoraEntrada()), 18)
                            + Texto.derecha(mon.tiempoTranscurrido(t), 10)
                            + Texto.derecha(Dinero.soles(mon.montoEstimado(t)), 11)
                            + mon.nivelAlerta(t).getEtiqueta());
                }
            }
            consola.info("[#] elegir vehiculo   [P] filtrar por placa   [T] filtrar por tipo   [L] limpiar filtros   [0] volver");
            String op = consola.leerTexto("Opcion: ").toUpperCase();
            if (op.equals("0") || op.isEmpty()) {
                return;
            } else if (op.equals("P")) {
                filtroPlaca = consola.leerTexto("Texto de la placa: ").toUpperCase();
            } else if (op.equals("T")) {
                filtroTipo = elegirTarifa(ctx.getBaseDatos().listarTarifas(), true);
            } else if (op.equals("L")) {
                filtroPlaca = "";
                filtroTipo = null;
            } else {
                int indice = parsearIndice(op, lista.size());
                if (indice < 0) {
                    consola.error("Opcion no valida.");
                } else {
                    final TicketCochera elegido = lista.get(indice);
                    ejecutar(() -> alSeleccionarVehiculo(elegido));
                }
            }
        }
    }

    /**
     * GANCHO (hook): qué pasa al elegir un vehículo en "Autos dentro".
     * Por defecto muestra el detalle; MenuEmpleado lo sobrescribe para cobrar directo.
     */
    protected void alSeleccionarVehiculo(TicketCochera ticket) throws CocheraException {
        mostrarCotizacion(ticket, ctx.getCalculadora().calcular(ticket, ctx.getReloj().ahora()));
    }

    protected void mostrarCotizacion(TicketCochera t, ResultadoTarifa r) {
        consola.subtitulo("DETALLE DE ESTADIA");
        consola.info("  Ticket:          " + t.getCodigoBarras());
        consola.info("  Placa:           " + t.getPlacaFormateada());
        consola.info("  Vehiculo:        " + t.getTarifa().getTipoVehiculo());
        consola.info("  Ingreso:         " + FechaUtil.fechaHora(t.getHoraEntrada()));
        consola.info("  Tiempo:          " + FechaUtil.formatoMinutos(r.getMinutos()));
        consola.info("  Tarifa base:     " + Dinero.soles(t.getTarifa().getCostoBase()) + " x " + r.getBloques()
                + " bloque(s) de 12 h = " + Dinero.soles(t.getTarifa().getCostoBase() * r.getBloques()));
        if (r.getHorasExtra() > 0) {
            boolean conTope = r.getMontoSobrecargo() < r.getHorasExtra() * t.getTarifa().getSobrecargoHora();
            consola.info("  Horas extra:     " + r.getHorasExtra() + " x " + Dinero.soles(t.getTarifa().getSobrecargoHora())
                    + " = " + Dinero.soles(r.getMontoSobrecargo()) + (conTope ? "  (tope: no supera la tarifa base)" : ""));
        } else if (r.getMinutos() >= 12 * 60) {
            consola.info("  Horas extra:     ninguna (dentro de la gracia de 30 min)");
        }
        consola.info("  TOTAL A COBRAR:  " + Dinero.soles(r.getTarifaTotal()));
    }

    // ─────────────────────── Pagos (compartido) ───────────────────────

    protected void mostrarTablaPagos(List<Pago> pagos, int numeroInicial) {
        if (pagos.isEmpty()) {
            consola.info("  No hay pagos para mostrar.");
            return;
        }
        consola.info(" #   " + Texto.derecha("Fecha/hora", 18) + Texto.derecha("Codigo", 16) + Texto.derecha("Placa", 9)
                + Texto.derecha("Tipo", 20) + Texto.derecha("Monto", 11) + Texto.derecha("Metodo", 10)
                + Texto.derecha("Cobrado por", 18) + "Ticket");
        for (int i = 0; i < pagos.size(); i++) {
            Pago p = pagos.get(i);
            TicketCochera t = p.getTicket();
            consola.info(Texto.izquierda(String.valueOf(numeroInicial + i), 2) + "   "
                    + Texto.derecha(FechaUtil.fechaHora(p.getCreadoEn()), 18)
                    + Texto.derecha(t.getCodigoBarras(), 16)
                    + Texto.derecha(t.getPlacaFormateada(), 9)
                    + Texto.columna(t.getTarifa().getTipoVehiculo(), 19) + " "
                    + Texto.derecha(Dinero.soles(p.getMontoTotal()), 11)
                    + Texto.derecha(p.getMetodoPago().getEtiqueta(), 10)
                    + Texto.columna(p.getTurno().getEmpleado().getNombreCompleto(), 17) + " "
                    + t.getEstado());
        }
    }

    protected void mostrarDetallePago(Pago p) {
        TicketCochera t = p.getTicket();
        consola.subtitulo("PAGO " + Texto.idCorto(p.getId()));
        consola.info("  Ticket:       " + t.getCodigoBarras() + " (" + t.getEstado() + ")");
        consola.info("  Placa:        " + t.getPlacaFormateada() + " - " + t.getTarifa().getTipoVehiculo());
        consola.info("  Entrada:      " + FechaUtil.fechaHora(t.getHoraEntrada()));
        consola.info("  Salida:       " + FechaUtil.fechaHora(t.getHoraSalida()));
        consola.info("  Monto:        " + Dinero.soles(p.getMontoTotal()));
        consola.info("  Metodo:       " + p.getMetodoPago());
        consola.info("  Cobrado por:  " + p.getTurno().getEmpleado().getNombreCompleto()
                + " (turno " + (p.getTurno().estaAbierto() ? "ABIERTO" : "CERRADO") + ")");
    }

    /** Submenú de un pago. Las subclases pueden agregar acciones (polimorfismo). */
    protected void gestionarPago(final Pago pago) {
        while (true) {
            mostrarDetallePago(pago);
            List<String> opciones = opcionesPago(pago);
            for (int i = 0; i < opciones.size(); i++) {
                consola.info("  " + (i + 1) + ". " + opciones.get(i));
            }
            consola.info("  0. Volver");
            final int op = consola.leerEntero("Opcion: ", 0, opciones.size());
            if (op == 0) {
                return;
            }
            ejecutar(() -> atenderOpcionPago(pago, op));
        }
    }

    protected List<String> opcionesPago(Pago pago) {
        return new ArrayList<>(Arrays.asList("Reimprimir ticket de salida", "Cambiar metodo de pago"));
    }

    protected void atenderOpcionPago(Pago pago, int opcion) throws CocheraException {
        switch (opcion) {
            case 1:
                imprimir(new TicketSalida(pago));
                break;
            case 2:
                consola.info("Metodo actual: " + pago.getMetodoPago() + ". Nuevo metodo:");
                MetodoPago nuevo = elegirMetodoPago();
                ctx.getCobros().cambiarMetodoPago(pago, nuevo);
                consola.exito("Metodo de pago actualizado a " + nuevo + ".");
                break;
            default:
                break;
        }
    }

    // ─────────────────────── helpers ───────────────────────

    /** "3" -> 2 (índice base 0); -1 si no es un número válido de la lista. */
    protected static int parsearIndice(String texto, int tamanio) {
        try {
            int n = Integer.parseInt(texto.trim());
            return n >= 1 && n <= tamanio ? n - 1 : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
