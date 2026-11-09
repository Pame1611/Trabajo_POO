package cochera.servicio;

import cochera.dto.CierreGeneral;
import cochera.dto.FilaExport;
import cochera.dto.ReporteTurno;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.TurnoCaja;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Exporta reportes a CSV (se abre directo en Excel). Reemplaza a SheetJS (xlsx) de la web.
 * Se escribe en UTF-8 con BOM para que Excel muestre bien las tildes.
 */
public class ExportadorCSV {

    private static final DateTimeFormatter SELLO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final File carpeta;

    public ExportadorCSV(File carpeta) {
        this.carpeta = carpeta;
    }

    public File exportarVentas(List<FilaExport> filas) throws IOException {
        List<String[]> contenido = new ArrayList<>();
        contenido.add(FilaExport.CABECERAS);
        double total = 0;
        for (FilaExport f : filas) {
            contenido.add(f.comoArreglo());
            total += f.getTotalCobrado();
        }
        contenido.add(new String[] {});
        contenido.add(new String[] {"TOTAL", "", "", "", "", "", "", "", "", "", Dinero.decimal(total)});
        return escribir("reporte_ventas_" + LocalDateTime.now().format(SELLO) + ".csv", contenido);
    }

    public File exportarCierre(CierreGeneral c) throws IOException {
        List<String[]> filas = new ArrayList<>();
        filas.add(new String[] {"CIERRE GENERAL 2 PM - COCHERA LOS ANGELES"});
        filas.add(new String[] {"Fecha de corte", FechaUtil.fechaIso(c.getFechaCorte())});
        filas.add(new String[] {"Desde", FechaUtil.fechaHora(c.getDesde())});
        filas.add(new String[] {"Hasta", FechaUtil.fechaHora(c.getHasta()) + ":59"});
        filas.add(new String[] {"Recaudacion total", Dinero.decimal(c.getResumen().getTotal())});
        filas.add(new String[] {"Total de pagos", String.valueOf(c.getResumen().getCantidad())});
        filas.add(new String[] {"Ticket promedio", Dinero.decimal(c.getTicketPromedio())});
        for (MetodoPago m : MetodoPago.values()) {
            filas.add(new String[] {m.getEtiqueta(), Dinero.decimal(c.getResumen().getPorMetodo(m))});
        }
        filas.add(new String[] {});
        filas.add(new String[] {"TURNOS", "Empleado", "Apertura", "Cierre", "Estado", "Efectivo declarado",
            "Diferencia caja", "Recaudado en corte"});
        for (ReporteTurno r : c.getTurnos()) {
            TurnoCaja t = r.getTurno();
            filas.add(new String[] {"", t.getEmpleado().getNombreCompleto(), FechaUtil.fechaHora(t.getFechaApertura()),
                FechaUtil.fechaHora(t.getFechaCierre()), t.getEstado().name(),
                t.getEfectivoDeclarado() == null ? "" : Dinero.decimal(t.getEfectivoDeclarado()),
                t.getDiferenciaCaja() == null ? "" : Dinero.decimal(t.getDiferenciaCaja()),
                Dinero.decimal(r.getRecaudado())});
        }
        filas.add(new String[] {});
        filas.add(new String[] {"PAGOS", "Fecha", "Codigo", "Placa", "Monto", "Metodo", "Empleado"});
        for (Pago p : c.getPagos()) {
            filas.add(new String[] {"", FechaUtil.fechaHora(p.getCreadoEn()), p.getTicket().getCodigoBarras(),
                p.getTicket().getPlacaFormateada(), Dinero.decimal(p.getMontoTotal()), p.getMetodoPago().getEtiqueta(),
                p.getTurno().getEmpleado().getNombreCompleto()});
        }
        return escribir("cierre_2pm_" + FechaUtil.fechaIso(c.getFechaCorte()) + ".csv", filas);
    }

    private File escribir(String nombre, List<String[]> filas) throws IOException {
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta " + carpeta.getPath());
        }
        File archivo = new File(carpeta, nombre);
        try (Writer w = new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            w.write('﻿'); // BOM para Excel
            for (String[] fila : filas) {
                StringBuilder linea = new StringBuilder();
                for (int i = 0; i < fila.length; i++) {
                    if (i > 0) {
                        linea.append(',');
                    }
                    linea.append(celda(fila[i]));
                }
                w.write(linea.toString());
                w.write("\r\n");
            }
        }
        return archivo;
    }

    private static String celda(String valor) {
        String v = valor == null ? "" : valor;
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }
}
