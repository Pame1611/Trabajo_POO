package cochera.gui;

import cochera.excepcion.CocheraException;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.servicio.MonitoreoServicio;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Pestaña "Autos dentro" (JPanel Form): vehículos EN_CURSO en vivo, con filtros por placa y tipo.
 * La usan cajero y administrador; solo el cajero recibe la acción de cobrar.
 */
public class PnlAutosDentro extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private static final String TODOS = "Todos los tipos";

    private final transient ContextoGui gui;
    private final transient Consumer<TicketCochera> alCobrar;
    private final ModeloTabla<TicketCochera> modelo;
    private transient List<TicketCochera> todos = new ArrayList<>();

    /**
     * @param alCobrar qué hacer al cobrar un vehículo; null si el usuario no cobra (administrador).
     */
    public PnlAutosDentro(ContextoGui gui, Consumer<TicketCochera> alCobrar) {
        this.gui = gui;
        this.alCobrar = alCobrar;
        initComponents();
        final MonitoreoServicio mon = gui.getApp().getMonitoreo();
        modelo = new ModeloTabla<TicketCochera>()
                .columna("Código", TicketCochera::getCodigoBarras)
                .columna("Placa", TicketCochera::getPlacaFormateada)
                .columna("Tipo de vehículo", t -> t.getTarifa().getTipoVehiculo())
                .columna("Ingreso", t -> FechaUtil.fechaHora(t.getHoraEntrada()))
                .columna("Tiempo dentro", mon::tiempoTranscurrido)
                .columna("A cobrar ahora", t -> Dinero.soles(mon.montoEstimado(t)))
                .columna("Alerta", t -> mon.nivelAlerta(t).getEtiqueta());
        tblAutos.setModel(modelo);
        Estilo.tabla(tblAutos);
        Estilo.anchos(tblAutos, 140, 90, 230, 140, 110, 120, 100);
        Estilo.derecha(tblAutos, 5);
        Estilo.alerta(tblAutos, 6);
        Estilo.botonSecundario(btnDetalle);
        Estilo.botonPrimario(btnCobrar);
        btnCobrar.setVisible(alCobrar != null);
        cboFiltroTipo.addItem(TODOS);
        for (Tarifa t : gui.getApp().getBaseDatos().listarTarifas()) {
            cboFiltroTipo.addItem(t.getTipoVehiculo());
        }
        cboFiltroTipo.addActionListener(e -> aplicarFiltro());
        UtilForm.alCambiar(txtFiltroPlaca, this::aplicarFiltro);
        tblAutos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (PnlAutosDentro.this.alCobrar != null) {
                        cobrarSeleccionado();
                    } else {
                        verDetalle();
                    }
                }
            }
        });
    }

    @Override
    public void refrescar() {
        try {
            todos = gui.getApp().getMonitoreo().listarAutosDentro();
            lblAviso.setText(" ");
        } catch (CocheraException e) {
            todos = new ArrayList<>();
            lblAviso.setText(e.getMessage());
        }
        aplicarFiltro();
    }

    @Override
    public boolean seActualizaSolo() {
        return true;
    }

    private void aplicarFiltro() {
        String tipoElegido = (String) cboFiltroTipo.getSelectedItem();
        Tarifa tipo = null;
        for (Tarifa t : gui.getApp().getBaseDatos().listarTarifas()) {
            if (t.getTipoVehiculo().equals(tipoElegido)) {
                tipo = t;
            }
        }
        List<TicketCochera> lista = gui.getApp().getMonitoreo().filtrar(todos, txtFiltroPlaca.getText(), tipo);
        modelo.setFilasConservandoSeleccion(tblAutos, lista);
        lblConteo.setText("   " + todos.size() + " vehículo(s) dentro"
                + (lista.size() != todos.size() ? "  ·  mostrando " + lista.size() : ""));
    }

    private TicketCochera seleccionado() {
        TicketCochera t = modelo.getSeleccionado(tblAutos);
        if (t == null) {
            Dialogos.info(this, "Selecciona un vehículo de la tabla.");
        }
        return t;
    }

    private void verDetalle() {
        TicketCochera t = seleccionado();
        if (t != null) {
            Dialogos.info(this, Dialogos.textoCotizacion(t,
                    gui.getApp().getCalculadora().calcular(t, gui.getApp().getReloj().ahora())));
        }
    }

    private void cobrarSeleccionado() {
        TicketCochera t = seleccionado();
        if (t != null && alCobrar != null) {
            alCobrar.accept(t);
        }
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlAutosDentro.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlFiltros = new javax.swing.JPanel();
        lblBuscar = new javax.swing.JLabel();
        txtFiltroPlaca = new javax.swing.JTextField();
        lblTipo = new javax.swing.JLabel();
        cboFiltroTipo = new javax.swing.JComboBox<>();
        lblConteo = new javax.swing.JLabel();
        scrAutos = new javax.swing.JScrollPane();
        tblAutos = new javax.swing.JTable();
        pnlAcciones = new javax.swing.JPanel();
        lblAviso = new javax.swing.JLabel();
        btnDetalle = new javax.swing.JButton();
        btnCobrar = new javax.swing.JButton();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(0, 12));

        pnlFiltros.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        pnlFiltros.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 6));

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblBuscar.setText("Placa:");
        pnlFiltros.add(lblBuscar);

        txtFiltroPlaca.setColumns(10);
        txtFiltroPlaca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnlFiltros.add(txtFiltroPlaca);

        lblTipo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTipo.setText("Tipo:");
        pnlFiltros.add(lblTipo);

        cboFiltroTipo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlFiltros.add(cboFiltroTipo);

        lblConteo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblConteo.setForeground(new java.awt.Color(180, 83, 9));
        lblConteo.setText(" ");
        pnlFiltros.add(lblConteo);

        add(pnlFiltros, java.awt.BorderLayout.NORTH);

        scrAutos.setViewportView(tblAutos);

        add(scrAutos, java.awt.BorderLayout.CENTER);

        pnlAcciones.setBackground(new java.awt.Color(244, 244, 245));
        pnlAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        lblAviso.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblAviso.setForeground(new java.awt.Color(146, 64, 14));
        lblAviso.setText(" ");
        pnlAcciones.add(lblAviso);

        btnDetalle.setText("Ver detalle");
        btnDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnDetalle);

        btnCobrar.setText("Cobrar salida del seleccionado");
        btnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnCobrar);

        add(pnlAcciones, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleActionPerformed
        verDetalle();
    }//GEN-LAST:event_btnDetalleActionPerformed

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        cobrarSeleccionado();
    }//GEN-LAST:event_btnCobrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCobrar;
    private javax.swing.JButton btnDetalle;
    private javax.swing.JComboBox<String> cboFiltroTipo;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblConteo;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlFiltros;
    private javax.swing.JScrollPane scrAutos;
    private javax.swing.JTable tblAutos;
    private javax.swing.JTextField txtFiltroPlaca;
    // End of variables declaration//GEN-END:variables
}
