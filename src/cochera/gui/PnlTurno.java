package cochera.gui;

import cochera.dto.ResumenPagos;
import cochera.excepcion.CocheraException;
import cochera.modelo.TurnoCaja;
import cochera.servicio.TurnoServicio;
import cochera.util.Dinero;
import cochera.util.FechaUtil;

/**
 * Pestaña "Turno de caja" (JPanel Form): abrir turno y cerrarlo con el arqueo.
 */
public class PnlTurno extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;

    public PnlTurno(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        Estilo.botonPrimario(btnAbrirTurno);
        Estilo.botonPeligro(btnCerrarTurno);
        UtilForm.alCambiar(txtConteo, this::actualizarDiferencia);
    }

    private TurnoServicio turnos() {
        return gui.getApp().getTurnos();
    }

    @Override
    public void refrescar() {
        TurnoCaja turno = turnos().obtenerTurnoAbierto();
        boolean abierto = turno != null;
        btnAbrirTurno.setVisible(!abierto);
        pnlKpis.setVisible(abierto);
        pnlArqueo.setVisible(abierto);
        if (!abierto) {
            lblEstado.setText("Sin turno abierto");
            lblEstado.setForeground(Estilo.AMBAR);
            lblDetalle.setText("Abre tu turno para registrar entradas y cobrar salidas. Todo lo que cobres quedará en tu turno.");
        } else {
            long minutos = FechaUtil.minutosEntre(turno.getFechaApertura(), gui.getApp().getReloj().ahora());
            lblEstado.setText("Turno abierto");
            lblEstado.setForeground(Estilo.VERDE);
            lblDetalle.setText("Desde " + FechaUtil.fechaHora(turno.getFechaApertura()) + "  ·  "
                    + FechaUtil.formatoMinutos(minutos) + " en caja");
            ResumenPagos r = turnos().obtenerResumenPagos(turno);
            lblVEfectivo.setText(Dinero.soles(r.getEfectivo()));
            lblVYape.setText(Dinero.soles(r.getYape()));
            lblVPlin.setText(Dinero.soles(r.getPlin()));
            lblVTotal.setText(Dinero.soles(r.getTotal()));
            lblVCobros.setText(String.valueOf(r.getCantidad()));
            actualizarDiferencia();
        }
        revalidate();
        repaint();
    }

    @Override
    public boolean seActualizaSolo() {
        return true;
    }

    /** Vista previa del arqueo mientras el cajero escribe su conteo. */
    private void actualizarDiferencia() {
        TurnoCaja turno = turnos().obtenerTurnoAbierto();
        if (turno == null) {
            return;
        }
        Double declarado;
        try {
            declarado = UtilForm.monto(txtConteo.getText(), "conteo");
        } catch (CocheraException e) {
            lblDiferencia.setText("Monto inválido");
            lblDiferencia.setForeground(Estilo.ROJO);
            return;
        }
        if (declarado == null) {
            lblDiferencia.setText("Diferencia: —");
            lblDiferencia.setForeground(Estilo.TEXTO_SUAVE);
            return;
        }
        double dif = turnos().previsualizarDiferencia(turno, declarado);
        String estado = dif > 0 ? "sobrante" : dif < 0 ? "faltante" : "caja cuadrada";
        lblDiferencia.setText("Diferencia: " + Dinero.conSigno(dif) + "  (" + estado + ")");
        lblDiferencia.setForeground(dif < 0 ? Estilo.ROJO : Estilo.VERDE);
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlTurno.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlEstado = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        lblDetalle = new javax.swing.JLabel();
        btnAbrirTurno = new javax.swing.JButton();
        pnlKpis = new javax.swing.JPanel();
        lblTEfectivo = new javax.swing.JLabel();
        lblTYape = new javax.swing.JLabel();
        lblTPlin = new javax.swing.JLabel();
        lblTTotal = new javax.swing.JLabel();
        lblTCobros = new javax.swing.JLabel();
        lblVEfectivo = new javax.swing.JLabel();
        lblVYape = new javax.swing.JLabel();
        lblVPlin = new javax.swing.JLabel();
        lblVTotal = new javax.swing.JLabel();
        lblVCobros = new javax.swing.JLabel();
        pnlArqueo = new javax.swing.JPanel();
        lblArqueoTitulo = new javax.swing.JLabel();
        lblArqueoNota = new javax.swing.JLabel();
        lblConteo = new javax.swing.JLabel();
        txtConteo = new javax.swing.JTextField();
        lblDiferencia = new javax.swing.JLabel();
        btnCerrarTurno = new javax.swing.JButton();
        lblRelleno = new javax.swing.JLabel();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.GridBagLayout());

        pnlEstado.setBackground(new java.awt.Color(255, 255, 255));
        pnlEstado.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlEstado.setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(113, 113, 122));
        lblTitulo.setText("TURNO DE CAJA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEstado.add(lblTitulo, gridBagConstraints);

        lblEstado.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(24, 24, 27));
        lblEstado.setText("Sin turno abierto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlEstado.add(lblEstado, gridBagConstraints);

        lblDetalle.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDetalle.setForeground(new java.awt.Color(113, 113, 122));
        lblDetalle.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlEstado.add(lblDetalle, gridBagConstraints);

        btnAbrirTurno.setText("Abrir turno de caja");
        btnAbrirTurno.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnAbrirTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirTurnoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlEstado.add(btnAbrirTurno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pnlEstado, gridBagConstraints);

        pnlKpis.setBackground(new java.awt.Color(255, 255, 255));
        pnlKpis.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        pnlKpis.setLayout(new java.awt.GridLayout(2, 5, 16, 2));

        lblTEfectivo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTEfectivo.setForeground(new java.awt.Color(113, 113, 122));
        lblTEfectivo.setText("EFECTIVO (SISTEMA)");
        pnlKpis.add(lblTEfectivo);

        lblTYape.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTYape.setForeground(new java.awt.Color(113, 113, 122));
        lblTYape.setText("YAPE");
        pnlKpis.add(lblTYape);

        lblTPlin.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTPlin.setForeground(new java.awt.Color(113, 113, 122));
        lblTPlin.setText("PLIN");
        pnlKpis.add(lblTPlin);

        lblTTotal.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTTotal.setForeground(new java.awt.Color(113, 113, 122));
        lblTTotal.setText("TOTAL COBRADO");
        pnlKpis.add(lblTTotal);

        lblTCobros.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTCobros.setForeground(new java.awt.Color(113, 113, 122));
        lblTCobros.setText("COBROS");
        pnlKpis.add(lblTCobros);

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

        lblVTotal.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVTotal.setForeground(new java.awt.Color(24, 24, 27));
        lblVTotal.setText("-");
        pnlKpis.add(lblVTotal);

        lblVCobros.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblVCobros.setForeground(new java.awt.Color(24, 24, 27));
        lblVCobros.setText("-");
        pnlKpis.add(lblVCobros);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(pnlKpis, gridBagConstraints);

        pnlArqueo.setBackground(new java.awt.Color(255, 255, 255));
        pnlArqueo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlArqueo.setLayout(new java.awt.GridBagLayout());

        lblArqueoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblArqueoTitulo.setText("Cerrar turno — arqueo de caja");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlArqueo.add(lblArqueoTitulo, gridBagConstraints);

        lblArqueoNota.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblArqueoNota.setForeground(new java.awt.Color(113, 113, 122));
        lblArqueoNota.setText("Cuenta el efectivo físico de la caja. Diferencia = tu conteo − efectivo del sistema (Yape y Plin son informativos).");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 12, 0);
        pnlArqueo.add(lblArqueoNota, gridBagConstraints);

        lblConteo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblConteo.setText("Conteo físico de efectivo (S/):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlArqueo.add(lblConteo, gridBagConstraints);

        txtConteo.setColumns(9);
        txtConteo.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlArqueo.add(txtConteo, gridBagConstraints);

        lblDiferencia.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblDiferencia.setForeground(new java.awt.Color(113, 113, 122));
        lblDiferencia.setText("Diferencia: —");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 0);
        pnlArqueo.add(lblDiferencia, gridBagConstraints);

        btnCerrarTurno.setText("Cerrar turno");
        btnCerrarTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarTurnoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlArqueo.add(btnCerrarTurno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(pnlArqueo, gridBagConstraints);

        lblRelleno.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblRelleno.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 1.0;
        add(lblRelleno, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbrirTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirTurnoActionPerformed
        gui.ejecutar(this, () -> {
            TurnoCaja turno = turnos().abrirTurno();
            Dialogos.info(this, "Turno abierto a las " + FechaUtil.hora(turno.getFechaApertura())
                    + ".\nYa puedes registrar entradas y cobrar salidas.");
        });
        refrescar();
    }//GEN-LAST:event_btnAbrirTurnoActionPerformed

    private void btnCerrarTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarTurnoActionPerformed
        gui.ejecutar(this, () -> {
            Double declarado = UtilForm.monto(txtConteo.getText(), "conteo de efectivo");
            if (declarado == null) {
                throw new CocheraException("Ingresa el monto de efectivo que contaste en caja.");
            }
            TurnoCaja turno = turnos().requerirTurnoAbierto("No tienes un turno abierto.");
            double dif = turnos().previsualizarDiferencia(turno, declarado);
            if (!Dialogos.confirmar(this, "¿Cerrar el turno declarando " + Dinero.soles(declarado) + " en efectivo?\n"
                    + "Diferencia de caja: " + Dinero.conSigno(dif))) {
                return;
            }
            TurnoCaja cerrado = turnos().cerrarTurno(declarado);
            txtConteo.setText("");
            Dialogos.info(this, "Turno cerrado a las " + FechaUtil.hora(cerrado.getFechaCierre())
                    + ".\nDiferencia registrada: " + Dinero.conSigno(cerrado.getDiferenciaCaja()));
        });
        refrescar();
    }//GEN-LAST:event_btnCerrarTurnoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirTurno;
    private javax.swing.JButton btnCerrarTurno;
    private javax.swing.JLabel lblArqueoNota;
    private javax.swing.JLabel lblArqueoTitulo;
    private javax.swing.JLabel lblConteo;
    private javax.swing.JLabel lblDetalle;
    private javax.swing.JLabel lblDiferencia;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblRelleno;
    private javax.swing.JLabel lblTCobros;
    private javax.swing.JLabel lblTEfectivo;
    private javax.swing.JLabel lblTPlin;
    private javax.swing.JLabel lblTTotal;
    private javax.swing.JLabel lblTYape;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVCobros;
    private javax.swing.JLabel lblVEfectivo;
    private javax.swing.JLabel lblVPlin;
    private javax.swing.JLabel lblVTotal;
    private javax.swing.JLabel lblVYape;
    private javax.swing.JPanel pnlArqueo;
    private javax.swing.JPanel pnlEstado;
    private javax.swing.JPanel pnlKpis;
    private javax.swing.JTextField txtConteo;
    // End of variables declaration//GEN-END:variables
}
