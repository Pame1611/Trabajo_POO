package cochera.gui;

import cochera.dto.CotizacionSalida;
import cochera.dto.KpiTurno;
import cochera.dto.ResumenPagos;
import cochera.excepcion.CocheraException;
import cochera.impresion.TicketSalida;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.servicio.CobrosServicio;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Pestaña "Mis cobros" (JPanel Form): KPIs del turno, pagos registrados, reimpresión y corrección del método.
 */
public class PnlMisCobros extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final ModeloTabla<Pago> modelo;
    private transient List<Pago> pagos = new ArrayList<>();

    public PnlMisCobros(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        modelo = new ModeloTabla<Pago>()
                .columna("Fecha y hora", p -> FechaUtil.fechaHora(p.getCreadoEn()))
                .columna("Código", p -> p.getTicket().getCodigoBarras())
                .columna("Placa", p -> p.getTicket().getPlacaFormateada())
                .columna("Tipo de vehículo", p -> p.getTicket().getTarifa().getTipoVehiculo())
                .columna("Monto", p -> Dinero.soles(p.getMontoTotal()))
                .columna("Método", Pago::getMetodoPago)
                .columna("Ticket", p -> p.getTicket().getEstado());
        tblPagos.setModel(modelo);
        Estilo.tabla(tblPagos);
        Estilo.anchos(tblPagos, 140, 140, 90, 220, 100, 90, 90);
        Estilo.derecha(tblPagos, 4);
        Estilo.botonSecundario(btnReimprimir);
        Estilo.botonSecundario(btnCambiarMetodo);
        Estilo.botonSecundario(btnConsultar);
        UtilForm.alCambiar(txtFiltro, this::aplicarFiltro);
    }

    @Override
    public void refrescar() {
        try {
            KpiTurno kpi = gui.getApp().getCobros().obtenerKpiTurnoActual();
            ResumenPagos r = kpi.getResumen();
            lblVTotal.setText(Dinero.soles(r.getTotal()));
            lblVEfectivo.setText(Dinero.soles(r.getEfectivo()));
            lblVYape.setText(Dinero.soles(r.getYape()));
            lblVPlin.setText(Dinero.soles(r.getPlin()));
            lblVCobros.setText(String.valueOf(kpi.getTicketsPagados()));
            lblVDentro.setText(String.valueOf(kpi.getTicketsEnCurso()));
            pagos = gui.getApp().getCobros().obtenerPagosTurno();
            lblAviso.setText(" ");
        } catch (CocheraException e) {
            pagos = new ArrayList<>();
            lblAviso.setText(e.getMessage());
        }
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        modelo.setFilasConservandoSeleccion(tblPagos, CobrosServicio.filtrarPorPlaca(pagos, txtFiltro.getText()));
    }

    private Pago seleccionado() {
        Pago p = modelo.getSeleccionado(tblPagos);
        if (p == null) {
            Dialogos.info(this, "Selecciona un pago de la tabla.");
        }
        return p;
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlMisCobros.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlKpis = new javax.swing.JPanel();
        lblTTotal = new javax.swing.JLabel();
        lblTEfectivo = new javax.swing.JLabel();
        lblTYape = new javax.swing.JLabel();
        lblTPlin = new javax.swing.JLabel();
        lblTCobros = new javax.swing.JLabel();
        lblTDentro = new javax.swing.JLabel();
        lblVTotal = new javax.swing.JLabel();
        lblVEfectivo = new javax.swing.JLabel();
        lblVYape = new javax.swing.JLabel();
        lblVPlin = new javax.swing.JLabel();
        lblVCobros = new javax.swing.JLabel();
        lblVDentro = new javax.swing.JLabel();
        scrPagos = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        pnlAcciones = new javax.swing.JPanel();
        lblFiltro = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnReimprimir = new javax.swing.JButton();
        btnCambiarMetodo = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        lblAviso = new javax.swing.JLabel();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(0, 12));

        pnlKpis.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpis.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlKpis.setLayout(new java.awt.GridLayout(2, 6, 16, 2));

        lblTTotal.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTTotal.setForeground(new java.awt.Color(113, 113, 122));
        lblTTotal.setText("TOTAL COBRADO");
        pnlKpis.add(lblTTotal);

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

        lblTCobros.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTCobros.setForeground(new java.awt.Color(113, 113, 122));
        lblTCobros.setText("TICKETS COBRADOS");
        pnlKpis.add(lblTCobros);

        lblTDentro.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTDentro.setForeground(new java.awt.Color(113, 113, 122));
        lblTDentro.setText("VEHÍCULOS DENTRO");
        pnlKpis.add(lblTDentro);

        lblVTotal.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVTotal.setForeground(new java.awt.Color(24, 24, 27));
        lblVTotal.setText("-");
        pnlKpis.add(lblVTotal);

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

        lblVCobros.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVCobros.setForeground(new java.awt.Color(24, 24, 27));
        lblVCobros.setText("-");
        pnlKpis.add(lblVCobros);

        lblVDentro.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVDentro.setForeground(new java.awt.Color(24, 24, 27));
        lblVDentro.setText("-");
        pnlKpis.add(lblVDentro);

        add(pnlKpis, java.awt.BorderLayout.NORTH);

        scrPagos.setViewportView(tblPagos);

        add(scrPagos, java.awt.BorderLayout.CENTER);

        pnlAcciones.setBackground(new java.awt.Color(244, 244, 245));
        pnlAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblFiltro.setText("Filtrar por placa:");
        pnlAcciones.add(lblFiltro);

        txtFiltro.setColumns(9);
        txtFiltro.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnlAcciones.add(txtFiltro);

        btnReimprimir.setText("Reimprimir ticket");
        btnReimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReimprimirActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnReimprimir);

        btnCambiarMetodo.setText("Cambiar método de pago");
        btnCambiarMetodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarMetodoActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnCambiarMetodo);

        btnConsultar.setText("¿Cuánto paga un vehículo?");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnConsultar);

        lblAviso.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblAviso.setForeground(new java.awt.Color(146, 64, 14));
        lblAviso.setText(" ");
        pnlAcciones.add(lblAviso);

        add(pnlAcciones, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnReimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReimprimirActionPerformed
        Pago p = seleccionado();
        if (p != null) {
            gui.imprimir(this, new TicketSalida(p));
        }
    }//GEN-LAST:event_btnReimprimirActionPerformed

    private void btnCambiarMetodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarMetodoActionPerformed
        final Pago p = seleccionado();
        if (p == null) {
            return;
        }
        final MetodoPago nuevo = Dialogos.elegirMetodoPago(this, p.getMetodoPago());
        if (nuevo != null && gui.ejecutar(this, () -> gui.getApp().getCobros().cambiarMetodoPago(p, nuevo))) {
            Dialogos.info(this, "Método de pago actualizado a " + nuevo + ".");
        }
        refrescar();
    }//GEN-LAST:event_btnCambiarMetodoActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        final String placa = Dialogos.pedirTexto(this, "Placa del vehículo:");
        if (placa != null && !placa.isEmpty()) {
            gui.ejecutar(this, () -> {
                CotizacionSalida c = gui.getApp().getCheckout().buscarTicket(null, placa);
                Dialogos.info(this, Dialogos.textoCotizacion(c.getTicket(), c.getTarifa()));
            });
        }
    }//GEN-LAST:event_btnConsultarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarMetodo;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnReimprimir;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblTCobros;
    private javax.swing.JLabel lblTDentro;
    private javax.swing.JLabel lblTEfectivo;
    private javax.swing.JLabel lblTPlin;
    private javax.swing.JLabel lblTTotal;
    private javax.swing.JLabel lblTYape;
    private javax.swing.JLabel lblVCobros;
    private javax.swing.JLabel lblVDentro;
    private javax.swing.JLabel lblVEfectivo;
    private javax.swing.JLabel lblVPlin;
    private javax.swing.JLabel lblVTotal;
    private javax.swing.JLabel lblVYape;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlKpis;
    private javax.swing.JScrollPane scrPagos;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
