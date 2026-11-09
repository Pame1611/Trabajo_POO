package cochera.gui;

import cochera.impresion.TicketEntrada;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Pestaña "Check-in" (JPanel Form): registra la entrada y genera el ticket con código de barras.
 */
public class PnlCheckin extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private transient TicketCochera ultimo;

    public PnlCheckin(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        Estilo.botonPrimario(btnRegistrar);
        Estilo.botonSecundario(btnReimprimir);
        btnReimprimir.setEnabled(false);
        FiltroPlaca.instalar(txtPlaca);
        cboTipo.setModel(new DefaultComboBoxModel<>(gui.getApp().getCheckin().obtenerTarifario().toArray(new Tarifa[0])));
        cboTipo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int i, boolean sel, boolean foco) {
                super.getListCellRendererComponent(lista, valor, i, sel, foco);
                if (valor instanceof Tarifa) {
                    Tarifa t = (Tarifa) valor;
                    setText(t.getTipoVehiculo() + "     " + Dinero.soles(t.getCostoBase()) + " / 12 h");
                }
                return this;
            }
        });
        cboTipo.addActionListener(e -> mostrarTarifa());
        cboTipo.setSelectedItem(gui.getApp().getBaseDatos().buscarTarifa(6)); // Auto
        mostrarTarifa();
    }

    @Override
    public void refrescar() {
        boolean hayTurno = gui.getApp().getTurnos().obtenerTurnoAbierto() != null;
        lblAviso.setVisible(!hayTurno);
        btnRegistrar.setEnabled(hayTurno);
        txtPlaca.setEnabled(hayTurno);
        if (hayTurno) {
            txtPlaca.requestFocusInWindow();
        }
    }

    private void mostrarTarifa() {
        Tarifa t = (Tarifa) cboTipo.getSelectedItem();
        lblTarifaInfo.setText(t == null ? " " : "Tarifa base " + Dinero.soles(t.getCostoBase())
                + " por bloque de 12 h  ·  " + Dinero.soles(t.getSobrecargoHora()) + " por hora extra");
    }

    private void registrar() {
        final Tarifa tarifa = (Tarifa) cboTipo.getSelectedItem();
        final String placa = txtPlaca.getText();
        gui.ejecutar(this, () -> {
            TicketCochera t = gui.getApp().getCheckin().registrarEntrada(placa, tarifa == null ? 0 : tarifa.getId());
            ultimo = t;
            lblUltimo.setText(t.getCodigoBarras() + "   ·   " + t.getPlacaFormateada() + "   ·   "
                    + t.getTarifa().getTipoVehiculo() + "   ·   " + FechaUtil.hora(t.getHoraEntrada()));
            btnReimprimir.setEnabled(true);
            txtPlaca.setText("");
            gui.imprimir(this, new TicketEntrada(t));
        });
        txtPlaca.requestFocusInWindow();
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlCheckin.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblAviso = new javax.swing.JLabel();
        pnlFormulario = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblPlaca = new javax.swing.JLabel();
        txtPlaca = new javax.swing.JTextField();
        lblPlacaNota = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        cboTipo = new javax.swing.JComboBox<>();
        lblTarifaInfo = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        pnlUltimo = new javax.swing.JPanel();
        lblUltimoTitulo = new javax.swing.JLabel();
        lblUltimo = new javax.swing.JLabel();
        btnReimprimir = new javax.swing.JButton();
        lblRelleno = new javax.swing.JLabel();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.GridBagLayout());

        lblAviso.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblAviso.setForeground(new java.awt.Color(146, 64, 14));
        lblAviso.setText("No tienes un turno de caja abierto. Ábrelo en la pestaña \"Turno de caja\".");
        lblAviso.setBackground(new java.awt.Color(254, 243, 199));
        lblAviso.setOpaque(true);
        lblAviso.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        add(lblAviso, gridBagConstraints);

        pnlFormulario.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormulario.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        pnlFormulario.setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitulo.setText("Registrar entrada de vehículo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlFormulario.add(lblTitulo, gridBagConstraints);

        lblPlaca.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPlaca.setText("Placa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlFormulario.add(lblPlaca, gridBagConstraints);

        txtPlaca.setColumns(9);
        txtPlaca.setFont(new java.awt.Font("Segoe UI", 0, 28)); // NOI18N
        txtPlaca.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        txtPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlFormulario.add(txtPlaca, gridBagConstraints);

        lblPlacaNota.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblPlacaNota.setForeground(new java.awt.Color(113, 113, 122));
        lblPlacaNota.setText("Se escribe sola en el formato ABC-123. Enter para registrar.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlFormulario.add(lblPlacaNota, gridBagConstraints);

        lblTipo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTipo.setText("Tipo de vehículo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlFormulario.add(lblTipo, gridBagConstraints);

        cboTipo.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlFormulario.add(cboTipo, gridBagConstraints);

        lblTarifaInfo.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblTarifaInfo.setForeground(new java.awt.Color(113, 113, 122));
        lblTarifaInfo.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlFormulario.add(lblTarifaInfo, gridBagConstraints);

        btnRegistrar.setText("Registrar entrada e imprimir ticket");
        btnRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        pnlFormulario.add(btnRegistrar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pnlFormulario, gridBagConstraints);

        pnlUltimo.setBackground(new java.awt.Color(255, 255, 255));
        pnlUltimo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        pnlUltimo.setLayout(new java.awt.GridBagLayout());

        lblUltimoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblUltimoTitulo.setForeground(new java.awt.Color(113, 113, 122));
        lblUltimoTitulo.setText("ÚLTIMO INGRESO REGISTRADO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlUltimo.add(lblUltimoTitulo, gridBagConstraints);

        lblUltimo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblUltimo.setText("Todavía no registras entradas en esta sesión.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlUltimo.add(lblUltimo, gridBagConstraints);

        btnReimprimir.setText("Reimprimir ticket");
        btnReimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReimprimirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlUltimo.add(btnReimprimir, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(pnlUltimo, gridBagConstraints);

        lblRelleno.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblRelleno.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 1.0;
        add(lblRelleno, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        registrar();
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        registrar();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnReimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReimprimirActionPerformed
        if (ultimo != null) {
            gui.imprimir(this, new TicketEntrada(ultimo));
        }
    }//GEN-LAST:event_btnReimprimirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnReimprimir;
    private javax.swing.JComboBox<cochera.modelo.Tarifa> cboTipo;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblPlaca;
    private javax.swing.JLabel lblPlacaNota;
    private javax.swing.JLabel lblRelleno;
    private javax.swing.JLabel lblTarifaInfo;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUltimo;
    private javax.swing.JLabel lblUltimoTitulo;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JPanel pnlUltimo;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
