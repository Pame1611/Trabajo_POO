package cochera.gui;

import cochera.dto.KpiHoy;
import cochera.dto.MontoPorTipo;
import cochera.excepcion.CocheraException;
import cochera.modelo.Pago;
import cochera.modelo.TurnoCaja;
import cochera.util.Dinero;
import cochera.util.FechaUtil;

/**
 * Pestaña "Resumen del día" del administrador (JPanel Form): KPIs, gráfico por hora,
 * trabajadores en turno, recaudación por tipo y últimos cobros. Se actualiza sola.
 */
public class PnlResumen extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final GraficoBarras grafico = new GraficoBarras();
    private final ModeloTabla<TurnoCaja> modeloTurnos;
    private final ModeloTabla<MontoPorTipo> modeloTipos;
    private final ModeloTabla<Pago> modeloRecientes;

    public PnlResumen(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        pnlBarras.add(grafico, java.awt.BorderLayout.CENTER);
        modeloTurnos = new ModeloTabla<TurnoCaja>()
                .columna("Empleado", t -> t.getEmpleado().getNombreCompleto())
                .columna("En caja desde", t -> FechaUtil.fechaHora(t.getFechaApertura()));
        modeloTipos = new ModeloTabla<MontoPorTipo>()
                .columna("Tipo de vehículo", MontoPorTipo::getTipoVehiculo)
                .columna("Recaudado", m -> Dinero.soles(m.getMonto()));
        modeloRecientes = new ModeloTabla<Pago>()
                .columna("Hora", p -> FechaUtil.hora(p.getCreadoEn()))
                .columna("Código", p -> p.getTicket().getCodigoBarras())
                .columna("Placa", p -> p.getTicket().getPlacaFormateada())
                .columna("Tipo de vehículo", p -> p.getTicket().getTarifa().getTipoVehiculo())
                .columna("Monto", p -> Dinero.soles(p.getMontoTotal()))
                .columna("Método", Pago::getMetodoPago)
                .columna("Cobrado por", p -> p.getTurno().getEmpleado().getNombreCompleto());
        tblTurnos.setModel(modeloTurnos);
        tblTipos.setModel(modeloTipos);
        tblRecientes.setModel(modeloRecientes);
        Estilo.tabla(tblTurnos);
        Estilo.tabla(tblTipos);
        Estilo.tabla(tblRecientes);
        Estilo.derecha(tblTipos, 1);
        Estilo.derecha(tblRecientes, 4);
        Estilo.anchos(tblRecientes, 60, 140, 90, 220, 90, 90, 160);
    }

    @Override
    public void refrescar() {
        gui.ejecutar(this, () -> {
            KpiHoy k = gui.getApp().getAdmin().obtenerKpiHoy();
            lblVAutos.setText(String.valueOf(k.getAutosDentro()));
            lblVIngresos.setText(Dinero.soles(k.getIngresosHoy()));
            lblVTickets.setText(String.valueOf(k.getTicketsHoy()));
            lblVEfectivo.setText(Dinero.soles(k.getResumen().getEfectivo()));
            lblVYape.setText(Dinero.soles(k.getResumen().getYape()));
            lblVPlin.setText(Dinero.soles(k.getResumen().getPlin()));
            grafico.setValores(k.getIngresosPorHora());
            modeloTipos.setFilas(k.getPorTipo());
            modeloRecientes.setFilas(k.getRegistrosRecientes());
            modeloTurnos.setFilas(gui.getApp().getAdmin().obtenerTurnosAbiertos());
        });
    }

    @Override
    public boolean seActualizaSolo() {
        return true;
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlResumen.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlKpis = new javax.swing.JPanel();
        lblTAutos = new javax.swing.JLabel();
        lblTIngresos = new javax.swing.JLabel();
        lblTTickets = new javax.swing.JLabel();
        lblTEfectivo = new javax.swing.JLabel();
        lblTYape = new javax.swing.JLabel();
        lblTPlin = new javax.swing.JLabel();
        lblVAutos = new javax.swing.JLabel();
        lblVIngresos = new javax.swing.JLabel();
        lblVTickets = new javax.swing.JLabel();
        lblVEfectivo = new javax.swing.JLabel();
        lblVYape = new javax.swing.JLabel();
        lblVPlin = new javax.swing.JLabel();
        pnlCentro = new javax.swing.JPanel();
        pnlGrafico = new javax.swing.JPanel();
        lblGraficoTitulo = new javax.swing.JLabel();
        pnlBarras = new javax.swing.JPanel();
        pnlLado = new javax.swing.JPanel();
        lblTurnosTitulo = new javax.swing.JLabel();
        scrTurnos = new javax.swing.JScrollPane();
        tblTurnos = new javax.swing.JTable();
        lblTiposTitulo = new javax.swing.JLabel();
        scrTipos = new javax.swing.JScrollPane();
        tblTipos = new javax.swing.JTable();
        pnlRecientes = new javax.swing.JPanel();
        lblRecientesTitulo = new javax.swing.JLabel();
        scrRecientes = new javax.swing.JScrollPane();
        tblRecientes = new javax.swing.JTable();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(0, 12));

        pnlKpis.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpis.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlKpis.setLayout(new java.awt.GridLayout(2, 6, 16, 2));

        lblTAutos.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTAutos.setForeground(new java.awt.Color(113, 113, 122));
        lblTAutos.setText("AUTOS DENTRO");
        pnlKpis.add(lblTAutos);

        lblTIngresos.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTIngresos.setForeground(new java.awt.Color(113, 113, 122));
        lblTIngresos.setText("INGRESOS DE HOY");
        pnlKpis.add(lblTIngresos);

        lblTTickets.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTTickets.setForeground(new java.awt.Color(113, 113, 122));
        lblTTickets.setText("TICKETS COBRADOS HOY");
        pnlKpis.add(lblTTickets);

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

        lblVAutos.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVAutos.setForeground(new java.awt.Color(24, 24, 27));
        lblVAutos.setText("-");
        pnlKpis.add(lblVAutos);

        lblVIngresos.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVIngresos.setForeground(new java.awt.Color(24, 24, 27));
        lblVIngresos.setText("-");
        pnlKpis.add(lblVIngresos);

        lblVTickets.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVTickets.setForeground(new java.awt.Color(24, 24, 27));
        lblVTickets.setText("-");
        pnlKpis.add(lblVTickets);

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

        add(pnlKpis, java.awt.BorderLayout.NORTH);

        pnlCentro.setBackground(new java.awt.Color(244, 244, 245));
        pnlCentro.setLayout(new java.awt.GridLayout(1, 2, 12, 0));

        pnlGrafico.setBackground(new java.awt.Color(255, 255, 255));
        pnlGrafico.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlGrafico.setLayout(new java.awt.BorderLayout(0, 8));

        lblGraficoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblGraficoTitulo.setText("Ingresos por hora (hoy)");
        pnlGrafico.add(lblGraficoTitulo, java.awt.BorderLayout.NORTH);

        pnlBarras.setBackground(new java.awt.Color(255, 255, 255));
        pnlBarras.setLayout(new java.awt.BorderLayout());

        pnlGrafico.add(pnlBarras, java.awt.BorderLayout.CENTER);

        pnlCentro.add(pnlGrafico);

        pnlLado.setBackground(new java.awt.Color(255, 255, 255));
        pnlLado.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlLado.setLayout(new java.awt.GridBagLayout());

        lblTurnosTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTurnosTitulo.setText("Trabajadores en turno");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlLado.add(lblTurnosTitulo, gridBagConstraints);

        scrTurnos.setViewportView(tblTurnos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlLado.add(scrTurnos, gridBagConstraints);

        lblTiposTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblTiposTitulo.setText("Recaudación por tipo de vehículo (hoy)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlLado.add(lblTiposTitulo, gridBagConstraints);

        scrTipos.setViewportView(tblTipos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlLado.add(scrTipos, gridBagConstraints);

        pnlCentro.add(pnlLado);

        add(pnlCentro, java.awt.BorderLayout.CENTER);

        pnlRecientes.setBackground(new java.awt.Color(255, 255, 255));
        pnlRecientes.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlRecientes.setPreferredSize(new java.awt.Dimension(100, 250));
        pnlRecientes.setLayout(new java.awt.BorderLayout(0, 8));

        lblRecientesTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRecientesTitulo.setText("Últimos cobros de hoy");
        pnlRecientes.add(lblRecientesTitulo, java.awt.BorderLayout.NORTH);

        scrRecientes.setViewportView(tblRecientes);

        pnlRecientes.add(scrRecientes, java.awt.BorderLayout.CENTER);

        add(pnlRecientes, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblGraficoTitulo;
    private javax.swing.JLabel lblRecientesTitulo;
    private javax.swing.JLabel lblTAutos;
    private javax.swing.JLabel lblTEfectivo;
    private javax.swing.JLabel lblTIngresos;
    private javax.swing.JLabel lblTPlin;
    private javax.swing.JLabel lblTTickets;
    private javax.swing.JLabel lblTYape;
    private javax.swing.JLabel lblTiposTitulo;
    private javax.swing.JLabel lblTurnosTitulo;
    private javax.swing.JLabel lblVAutos;
    private javax.swing.JLabel lblVEfectivo;
    private javax.swing.JLabel lblVIngresos;
    private javax.swing.JLabel lblVPlin;
    private javax.swing.JLabel lblVTickets;
    private javax.swing.JLabel lblVYape;
    private javax.swing.JPanel pnlBarras;
    private javax.swing.JPanel pnlCentro;
    private javax.swing.JPanel pnlGrafico;
    private javax.swing.JPanel pnlKpis;
    private javax.swing.JPanel pnlLado;
    private javax.swing.JPanel pnlRecientes;
    private javax.swing.JScrollPane scrRecientes;
    private javax.swing.JScrollPane scrTipos;
    private javax.swing.JScrollPane scrTurnos;
    private javax.swing.JTable tblRecientes;
    private javax.swing.JTable tblTipos;
    private javax.swing.JTable tblTurnos;
    // End of variables declaration//GEN-END:variables
}
