package cochera.gui;

import cochera.dto.CierreGeneral;
import cochera.dto.ReporteTurno;
import cochera.excepcion.ValidacionException;
import cochera.modelo.Pago;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Pestaña "Cierre 2 PM" (JPanel Form): cierre operativo de 24 h, desde las 14:00 del día anterior
 * hasta las 13:59:59 del día de corte, con turnos, pagos y exportación.
 */
public class PnlCierre extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final ModeloTabla<ReporteTurno> modeloTurnos;
    private final ModeloTabla<Pago> modeloPagos;
    private transient CierreGeneral cierre;

    public PnlCierre(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        modeloTurnos = new ModeloTabla<ReporteTurno>()
                .columna("Empleado", r -> r.getTurno().getEmpleado().getNombreCompleto())
                .columna("Apertura", r -> FechaUtil.fechaHora(r.getTurno().getFechaApertura()))
                .columna("Cierre", r -> FechaUtil.fechaHora(r.getTurno().getFechaCierre()))
                .columna("Estado", r -> r.getTurno().getEstado())
                .columna("Efectivo declarado", r -> r.getTurno().getEfectivoDeclarado() == null ? "-"
                        : Dinero.soles(r.getTurno().getEfectivoDeclarado()))
                .columna("Diferencia", r -> r.getTurno().getDiferenciaCaja() == null ? "-"
                        : Dinero.conSigno(r.getTurno().getDiferenciaCaja()))
                .columna("Cobrado en el corte", r -> Dinero.soles(r.getRecaudado()));
        modeloPagos = new ModeloTabla<Pago>()
                .columna("Fecha y hora", p -> FechaUtil.fechaHora(p.getCreadoEn()))
                .columna("Código", p -> p.getTicket().getCodigoBarras())
                .columna("Placa", p -> p.getTicket().getPlacaFormateada())
                .columna("Tipo de vehículo", p -> p.getTicket().getTarifa().getTipoVehiculo())
                .columna("Monto", p -> Dinero.soles(p.getMontoTotal()))
                .columna("Método", Pago::getMetodoPago)
                .columna("Cobrado por", p -> p.getTurno().getEmpleado().getNombreCompleto());
        tblTurnos.setModel(modeloTurnos);
        tblPagos.setModel(modeloPagos);
        Estilo.tabla(tblTurnos);
        Estilo.tabla(tblPagos);
        Estilo.derecha(tblTurnos, 4, 5, 6);
        Estilo.derecha(tblPagos, 4);
        Estilo.botonPrimario(btnConsultar);
        Estilo.botonSecundario(btnExportar);
        txtFecha.setText(FechaUtil.fechaIso(gui.getApp().getReloj().ahora()));
    }

    @Override
    public void refrescar() {
        if (cierre == null) {
            consultar();
        }
    }

    private void consultar() {
        gui.ejecutar(this, () -> {
            LocalDate fecha = UtilForm.fecha(txtFecha, "fecha de corte");
            cierre = gui.getApp().getAdmin().obtenerCierreGeneral(fecha);
            lblPeriodo.setText("Periodo: " + FechaUtil.fechaHora(cierre.getDesde()) + " a "
                    + FechaUtil.fechaHora(cierre.getHasta()) + ":59");
            lblVRecaudacion.setText(Dinero.soles(cierre.getResumen().getTotal()));
            lblVPagos.setText(String.valueOf(cierre.getResumen().getCantidad()));
            lblVPromedio.setText(Dinero.soles(cierre.getTicketPromedio()));
            lblVEfectivo.setText(Dinero.soles(cierre.getResumen().getEfectivo()));
            lblVYape.setText(Dinero.soles(cierre.getResumen().getYape()));
            lblVPlin.setText(Dinero.soles(cierre.getResumen().getPlin()));
            modeloTurnos.setFilas(cierre.getTurnos());
            modeloPagos.setFilas(cierre.getPagos());
        });
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlCierre.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlFiltro = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        btnConsultar = new javax.swing.JButton();
        btnExportar = new javax.swing.JButton();
        lblPeriodo = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        pnlKpis = new javax.swing.JPanel();
        lblTRecaudacion = new javax.swing.JLabel();
        lblTPagos = new javax.swing.JLabel();
        lblTPromedio = new javax.swing.JLabel();
        lblTEfectivo = new javax.swing.JLabel();
        lblTYape = new javax.swing.JLabel();
        lblTPlin = new javax.swing.JLabel();
        lblVRecaudacion = new javax.swing.JLabel();
        lblVPagos = new javax.swing.JLabel();
        lblVPromedio = new javax.swing.JLabel();
        lblVEfectivo = new javax.swing.JLabel();
        lblVYape = new javax.swing.JLabel();
        lblVPlin = new javax.swing.JLabel();
        pnlTablas = new javax.swing.JPanel();
        pnlTurnos = new javax.swing.JPanel();
        lblTurnosTitulo = new javax.swing.JLabel();
        scrTurnos = new javax.swing.JScrollPane();
        tblTurnos = new javax.swing.JTable();
        pnlPagos = new javax.swing.JPanel();
        lblPagosTitulo = new javax.swing.JLabel();
        scrPagos = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(0, 12));

        pnlFiltro.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltro.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        pnlFiltro.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 6));

        lblFecha.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblFecha.setText("Fecha de corte:");
        pnlFiltro.add(lblFecha);

        txtFecha.setColumns(10);
        txtFecha.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });
        pnlFiltro.add(txtFecha);

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        pnlFiltro.add(btnConsultar);

        btnExportar.setText("Exportar a Excel (CSV)");
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });
        pnlFiltro.add(btnExportar);

        lblPeriodo.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblPeriodo.setForeground(new java.awt.Color(113, 113, 122));
        lblPeriodo.setText(" ");
        pnlFiltro.add(lblPeriodo);

        add(pnlFiltro, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(244, 244, 245));
        pnlCentro.setLayout(new java.awt.BorderLayout(0, 12));

        pnlKpis.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpis.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlKpis.setLayout(new java.awt.GridLayout(2, 6, 16, 2));

        lblTRecaudacion.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTRecaudacion.setForeground(new java.awt.Color(113, 113, 122));
        lblTRecaudacion.setText("RECAUDACIÓN TOTAL");
        pnlKpis.add(lblTRecaudacion);

        lblTPagos.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTPagos.setForeground(new java.awt.Color(113, 113, 122));
        lblTPagos.setText("PAGOS");
        pnlKpis.add(lblTPagos);

        lblTPromedio.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTPromedio.setForeground(new java.awt.Color(113, 113, 122));
        lblTPromedio.setText("TICKET PROMEDIO");
        pnlKpis.add(lblTPromedio);

        lblTEfectivo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTEfectivo.setForeground(new java.awt.Color(113, 113, 122));
        lblTEfectivo.setText("EFECTIVO");
        pnlKpis.add(lblTEfectivo);

        lblTYape.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTYape.setForeground(new java.awt.Color(113, 113, 122));
        lblTYape.setText("YAPE");
        pnlKpis.add(lblTYape);

        lblTPlin.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTPlin.setForeground(new java.awt.Color(113, 113, 122));
        lblTPlin.setText("PLIN");
        pnlKpis.add(lblTPlin);

        lblVRecaudacion.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVRecaudacion.setForeground(new java.awt.Color(24, 24, 27));
        lblVRecaudacion.setText("-");
        pnlKpis.add(lblVRecaudacion);

        lblVPagos.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVPagos.setForeground(new java.awt.Color(24, 24, 27));
        lblVPagos.setText("-");
        pnlKpis.add(lblVPagos);

        lblVPromedio.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVPromedio.setForeground(new java.awt.Color(24, 24, 27));
        lblVPromedio.setText("-");
        pnlKpis.add(lblVPromedio);

        lblVEfectivo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVEfectivo.setForeground(new java.awt.Color(24, 24, 27));
        lblVEfectivo.setText("-");
        pnlKpis.add(lblVEfectivo);

        lblVYape.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVYape.setForeground(new java.awt.Color(24, 24, 27));
        lblVYape.setText("-");
        pnlKpis.add(lblVYape);

        lblVPlin.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVPlin.setForeground(new java.awt.Color(24, 24, 27));
        lblVPlin.setText("-");
        pnlKpis.add(lblVPlin);

        pnlCentro.add(pnlKpis, java.awt.BorderLayout.NORTH);

        pnlTablas.setBackground(new java.awt.Color(244, 244, 245));
        pnlTablas.setLayout(new java.awt.GridLayout(2, 1, 0, 12));

        pnlTurnos.setBackground(new java.awt.Color(255, 255, 255));
        pnlTurnos.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlTurnos.setLayout(new java.awt.BorderLayout(0, 8));

        lblTurnosTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTurnosTitulo.setText("Turnos del corte (apertura, cierre y arqueo)");
        pnlTurnos.add(lblTurnosTitulo, java.awt.BorderLayout.NORTH);

        scrTurnos.setViewportView(tblTurnos);

        pnlTurnos.add(scrTurnos, java.awt.BorderLayout.CENTER);

        pnlTablas.add(pnlTurnos);

        pnlPagos.setBackground(new java.awt.Color(255, 255, 255));
        pnlPagos.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlPagos.setLayout(new java.awt.BorderLayout(0, 8));

        lblPagosTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblPagosTitulo.setText("Pagos del corte");
        pnlPagos.add(lblPagosTitulo, java.awt.BorderLayout.NORTH);

        scrPagos.setViewportView(tblPagos);

        pnlPagos.add(scrPagos, java.awt.BorderLayout.CENTER);

        pnlTablas.add(pnlPagos);

        pnlCentro.add(pnlTablas, java.awt.BorderLayout.CENTER);

        add(pnlCentro, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        consultar();
    }//GEN-LAST:event_txtFechaActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        consultar();
        if (cierre == null) {
            return;
        }
        try {
            File archivo = gui.getApp().getExportador().exportarCierre(cierre);
            Dialogos.info(this, "Archivo generado (se abre con Excel):\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            Dialogos.error(this, "No se pudo exportar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnExportarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnExportar;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblPagosTitulo;
    private javax.swing.JLabel lblPeriodo;
    private javax.swing.JLabel lblTEfectivo;
    private javax.swing.JLabel lblTPagos;
    private javax.swing.JLabel lblTPlin;
    private javax.swing.JLabel lblTPromedio;
    private javax.swing.JLabel lblTRecaudacion;
    private javax.swing.JLabel lblTYape;
    private javax.swing.JLabel lblTurnosTitulo;
    private javax.swing.JLabel lblVEfectivo;
    private javax.swing.JLabel lblVPagos;
    private javax.swing.JLabel lblVPlin;
    private javax.swing.JLabel lblVPromedio;
    private javax.swing.JLabel lblVRecaudacion;
    private javax.swing.JLabel lblVYape;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlKpis;
    private javax.swing.JPanel pnlPagos;
    private javax.swing.JPanel pnlTablas;
    private javax.swing.JPanel pnlTurnos;
    private javax.swing.JScrollPane scrPagos;
    private javax.swing.JScrollPane scrTurnos;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTable tblTurnos;
    private javax.swing.JTextField txtFecha;
    // End of variables declaration//GEN-END:variables
}
