package cochera.prueba;

import cochera.ContextoAplicacion;
import cochera.dto.CierreGeneral;
import cochera.dto.CotizacionSalida;
import cochera.dto.FilaExport;
import cochera.dto.KpiHoy;
import cochera.dto.ResultadoCobro;
import cochera.excepcion.AccesoDenegadoException;
import cochera.excepcion.ValidacionException;
import cochera.modelo.EstadoTicket;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.TicketCochera;
import cochera.modelo.TurnoCaja;
import cochera.servicio.AdminServicio;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Recorre el flujo completo usando solo los servicios (sin menús):
 * login, turnos, check-in, check-out con traslape de turnos, vuelto, arqueo y reportes del admin.
 * Ejecutar: java -cp bin cochera.prueba.PruebaFlujoOperativo
 */
public class PruebaFlujoOperativo {

    public static void main(String[] args) throws Exception {
        final Verificador v = new Verificador();
        final ContextoAplicacion ctx = new ContextoAplicacion(false);

        System.out.println("=== Acceso y turnos ===");
        v.lanza(AccesoDenegadoException.class, () -> ctx.getAuth().iniciarSesion("cajero1@cochera.pe", "malaclave"),
                "Clave incorrecta rechazada");
        ctx.getAuth().iniciarSesion("admin@cochera.pe", "admin123");
        v.lanza(AccesoDenegadoException.class, () -> ctx.getTurnos().abrirTurno(), "El ADMIN no puede abrir turno");
        ctx.getAuth().cerrarSesion();

        ctx.getAuth().iniciarSesion("cajero1@cochera.pe", "cajero123");
        v.lanza(AccesoDenegadoException.class, () -> ctx.getCheckin().registrarEntrada("ABC123", 6),
                "Sin turno abierto no hay check-in");
        TurnoCaja turno1 = ctx.getTurnos().abrirTurno();
        v.lanza(ValidacionException.class, () -> ctx.getTurnos().abrirTurno(), "No se abren dos turnos a la vez");
        v.lanza(AccesoDenegadoException.class, () -> ctx.getAdmin().obtenerKpiHoy(), "El cajero no ve el panel admin");

        System.out.println("=== Check-in ===");
        TicketCochera abc = ctx.getCheckin().registrarEntrada("abc-123", 6);
        v.verdadero(abc.getPlaca().equals("ABC123") && abc.getCodigoBarras().matches("TK-\\d{4}-000001"),
                "Placa normalizada y codigo " + abc.getCodigoBarras());
        v.lanza(ValidacionException.class, () -> ctx.getCheckin().registrarEntrada("ABC123", 6),
                "Placa duplicada EN_CURSO rechazada");
        v.lanza(ValidacionException.class, () -> ctx.getCheckin().registrarEntrada("A1", 6), "Placa demasiado corta");
        v.lanza(ValidacionException.class, () -> ctx.getCheckin().registrarEntrada("QWE111", 99), "Tipo de vehiculo invalido");

        System.out.println("=== Paso del tiempo: 13 horas ===");
        ctx.getReloj().adelantarMinutos(13 * 60);
        CotizacionSalida cot = ctx.getCheckout().buscar("abc123");
        v.igual(7.00, cot.getTarifa().getTarifaTotal(), "Auto 13h = 6 + 1 hora extra");
        v.verdadero(ctx.getCheckout().sugerirPlacas("AB").contains("ABC-123"), "Autocompletado de placas");

        System.out.println("=== Traslape de turnos: cajero1 registra, cajero2 cobra ===");
        TicketCochera xyz = ctx.getCheckin().registrarEntrada("XYZ789", 6);
        ctx.getAuth().cerrarSesion();
        ctx.getAuth().iniciarSesion("cajero2@cochera.pe", "cajero123");
        TurnoCaja turno2 = ctx.getTurnos().abrirTurno();
        ResultadoCobro cobroXyz = ctx.getCheckout().registrarPago(
                ctx.getCheckout().buscar(xyz.getCodigoBarras()).getTicket(), MetodoPago.YAPE, null);
        v.verdadero(cobroXyz.getPago().getTurno() == turno2, "El pago queda en el turno de quien COBRA (cajero2)");
        v.lanza(ValidacionException.class, () -> ctx.getCheckout().buscar(xyz.getCodigoBarras()),
                "Ticket ya cobrado no se cobra dos veces");
        ctx.getAuth().cerrarSesion();

        System.out.println("=== Cobro en efectivo con vuelto ===");
        ctx.getAuth().iniciarSesion("cajero1@cochera.pe", "cajero123");
        v.lanza(ValidacionException.class,
                () -> ctx.getCheckout().registrarPago(abc, MetodoPago.EFECTIVO, 5.0), "Monto recibido insuficiente");
        v.verdadero(abc.estaEnCurso(), "Tras el error el ticket sigue EN_CURSO");
        ResultadoCobro cobroAbc = ctx.getCheckout().registrarPago(abc, MetodoPago.EFECTIVO, 10.0);
        v.igual(3.00, cobroAbc.getVuelto(), "Vuelto de S/ 10.00 sobre S/ 7.00");
        v.verdadero(abc.getEstado() == EstadoTicket.PAGADO && abc.getHoraSalida() != null, "Ticket PAGADO con hora de salida");

        System.out.println("=== Arqueo y cierre de turno ===");
        v.igual(-2.00, ctx.getTurnos().previsualizarDiferencia(turno1, 5.0), "Vista previa: faltan S/ 2.00");
        v.lanza(ValidacionException.class, () -> ctx.getTurnos().cerrarTurno(-1), "Efectivo negativo rechazado");
        ctx.getTurnos().cerrarTurno(7.0);
        v.igual(0.00, turno1.getDiferenciaCaja(), "Caja cuadrada al declarar S/ 7.00");
        v.lanza(AccesoDenegadoException.class,
                () -> ctx.getCobros().cambiarMetodoPago(cobroAbc.getPago(), MetodoPago.PLIN),
                "Con el turno cerrado el cajero ya no corrige pagos");
        ctx.getAuth().cerrarSesion();

        System.out.println("=== Concurrencia: 2 cajeros registran 40 entradas a la vez ===");
        ctx.getAuth().iniciarSesion("cajero2@cochera.pe", "cajero123");
        final List<String> codigos = Collections.synchronizedList(new ArrayList<String>());
        Thread[] hilos = new Thread[2];
        for (int h = 0; h < hilos.length; h++) {
            final int base = h * 20;
            hilos[h] = new Thread(() -> {
                for (int i = 0; i < 20; i++) {
                    try {
                        codigos.add(ctx.getCheckin().registrarEntrada(String.format("P%05d", base + i), 1).getCodigoBarras());
                    } catch (Exception e) {
                        codigos.add("ERROR " + e.getMessage());
                    }
                }
            });
            hilos[h].start();
        }
        for (Thread t : hilos) {
            t.join();
        }
        Set<String> unicos = new HashSet<>(codigos);
        int errores = 0;
        for (String c : codigos) {
            if (c.startsWith("ERROR")) {
                errores++;
                System.out.println("     " + c);
            }
        }
        v.verdadero(errores == 0, "Ningun check-in fallo (" + errores + " errores)");
        v.verdadero(codigos.size() == 40 && unicos.size() == 40, "40 codigos de ticket, todos distintos");
        ctx.getAuth().cerrarSesion();

        System.out.println("=== Panel del administrador ===");
        ctx.getAuth().iniciarSesion("admin@cochera.pe", "admin123");
        AdminServicio admin = ctx.getAdmin();
        KpiHoy kpi = admin.obtenerKpiHoy();
        v.igual(13.00, kpi.getIngresosHoy(), "Ingresos de hoy (7 efectivo + 6 Yape)");
        v.verdadero(kpi.getAutosDentro() == 40, "Autos dentro = 40 (obtenido " + kpi.getAutosDentro() + ")");
        v.verdadero(admin.obtenerTurnosAbiertos().size() == 1, "Un trabajador en turno (cajero2)");
        v.verdadero(admin.obtenerTurnosCerrados(null, null, 1).getTotal() == 1, "Un turno cerrado en auditoria");

        LocalDateTime horaPago = cobroAbc.getPago().getCreadoEn();
        LocalDate fechaCorte = horaPago.getHour() >= 14 ? horaPago.toLocalDate().plusDays(1) : horaPago.toLocalDate();
        CierreGeneral cierre = admin.obtenerCierreGeneral(fechaCorte);
        v.igual(13.00, cierre.getResumen().getTotal(), "Cierre 2 PM del " + fechaCorte + " recauda S/ 13.00");
        v.verdadero(cierre.getTurnos().size() == 2, "El cierre incluye los 2 turnos que cobraron");

        List<FilaExport> filas = admin.obtenerFilasExport(null, null, null, "Maria Torres");
        v.verdadero(filas.size() == 1 && filas.get(0).getPlaca().equals("XYZ-789"), "Filtro de exportacion por empleado");
        File carpeta = Files.createTempDirectory("cochera").toFile();
        File csv = new cochera.servicio.ExportadorCSV(carpeta).exportarVentas(admin.obtenerFilasExport(null, null, null, null));
        v.verdadero(csv.exists() && Files.readAllLines(csv.toPath()).size() == 5, "CSV generado: " + csv.getName());

        Pago pagoAbc = cobroAbc.getPago();
        ctx.getCobros().cambiarMetodoPago(pagoAbc, MetodoPago.PLIN);
        v.verdadero(pagoAbc.getMetodoPago() == MetodoPago.PLIN, "El admin corrige el metodo de pago");
        admin.anularTicket(abc);
        v.verdadero(abc.getEstado() == EstadoTicket.ANULADO, "El admin anula un ticket");
        v.lanza(ValidacionException.class, () -> admin.anularTicket(abc), "No se anula dos veces");
        v.lanza(ValidacionException.class, () -> admin.actualizarTarifa(6, -1, 1), "Tarifa negativa rechazada");
        v.igual(7.00, admin.actualizarTarifa(6, 7, 1).getCostoBase(), "Tarifa de Auto actualizada a S/ 7.00");

        v.resumen();
        System.exit(v.getFallos() == 0 ? 0 : 1);
    }
}
