package cochera.gui;

import cochera.datos.DatosIniciales;
import cochera.modelo.Usuario;
import cochera.util.FechaUtil;
import cochera.util.Texto;

/**
 * Diálogo de herramientas para la exposición (JDialog Form): reloj, impresora y usuarios de prueba.
 */
public class DlgDemostracion extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final javax.swing.ButtonGroup grupoImpresora = new javax.swing.ButtonGroup();

    public DlgDemostracion(java.awt.Window duenio, ContextoGui gui) {
        super(duenio, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.gui = gui;
        initComponents();
        spnHoras.setModel(new javax.swing.SpinnerNumberModel(13, 0, 72, 1));
        spnMinutos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 5));
        grupoImpresora.add(rbPantalla);
        grupoImpresora.add(rbArchivo);
        Estilo.botonPrimario(btnAdelantar);
        Estilo.botonSecundario(btnReiniciar);
        Estilo.botonSecundario(btnCerrar);
        cargarUsuarios();
        actualizar();
        setLocationRelativeTo(duenio);
    }

    private void actualizar() {
        long desfase = gui.getApp().getReloj().getMinutosDesfase();
        lblReloj.setText(FechaUtil.fechaHora(gui.getApp().getReloj().ahora())
                + (desfase > 0 ? "   (adelantado " + FechaUtil.formatoMinutos(desfase) + ")" : "   (hora real de Lima)"));
        boolean archivo = gui.getApp().usaImpresoraArchivo();
        rbArchivo.setSelected(archivo);
        rbPantalla.setSelected(!archivo);
        gui.notificarCambio();
    }

    private void cargarUsuarios() {
        StringBuilder sb = new StringBuilder();
        for (Usuario u : gui.getApp().getBaseDatos().listarUsuarios()) {
            String clave = u.getEmail().endsWith("@cochera.pe") && u.getEmail().startsWith("admin")
                    ? DatosIniciales.CLAVE_DEMO_ADMIN
                    : (u.getEmail().startsWith("cajero1@") || u.getEmail().startsWith("cajero2@")
                    ? DatosIniciales.CLAVE_DEMO_CAJERO : "(la asignada)");
            sb.append(Texto.derecha(u.getEmail(), 24)).append(Texto.derecha(clave, 14))
                    .append(u.getNombreCompleto()).append(" (").append(u.getRol()).append(")\n");
        }
        txtUsuarios.setText(sb.toString());
        txtUsuarios.setCaretPosition(0);
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de DlgDemostracion.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlContenido = new javax.swing.JPanel();
        lblRelojTitulo = new javax.swing.JLabel();
        lblReloj = new javax.swing.JLabel();
        lblRelojNota = new javax.swing.JLabel();
        pnlAdelantar = new javax.swing.JPanel();
        lblHoras = new javax.swing.JLabel();
        spnHoras = new javax.swing.JSpinner();
        lblMinutos = new javax.swing.JLabel();
        spnMinutos = new javax.swing.JSpinner();
        btnAdelantar = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();
        lblImpresoraTitulo = new javax.swing.JLabel();
        pnlImpresora = new javax.swing.JPanel();
        rbPantalla = new javax.swing.JRadioButton();
        rbArchivo = new javax.swing.JRadioButton();
        lblUsuariosTitulo = new javax.swing.JLabel();
        scrUsuarios = new javax.swing.JScrollPane();
        txtUsuarios = new javax.swing.JTextArea();
        pnlBotones = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Herramientas de demostración");
        getContentPane().setLayout(new java.awt.BorderLayout());

        pnlContenido.setBackground(new java.awt.Color(255, 255, 255));
        pnlContenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 16, 24));
        pnlContenido.setLayout(new java.awt.GridBagLayout());

        lblRelojTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblRelojTitulo.setText("Reloj del sistema");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlContenido.add(lblRelojTitulo, gridBagConstraints);

        lblReloj.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblReloj.setForeground(new java.awt.Color(180, 83, 9));
        lblReloj.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContenido.add(lblReloj, gridBagConstraints);

        lblRelojNota.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblRelojNota.setForeground(new java.awt.Color(113, 113, 122));
        lblRelojNota.setText("Adelanta el reloj para mostrar los cobros de más de 12 horas sin esperar.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 6, 0);
        pnlContenido.add(lblRelojNota, gridBagConstraints);

        pnlAdelantar.setBackground(new java.awt.Color(255, 255, 255));
        pnlAdelantar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 2));

        lblHoras.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblHoras.setText("Horas:");
        pnlAdelantar.add(lblHoras);

        spnHoras.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnlAdelantar.add(spnHoras);

        lblMinutos.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblMinutos.setText("Minutos:");
        pnlAdelantar.add(lblMinutos);

        spnMinutos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnlAdelantar.add(spnMinutos);

        btnAdelantar.setText("Adelantar");
        btnAdelantar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdelantarActionPerformed(evt);
            }
        });
        pnlAdelantar.add(btnAdelantar);

        btnReiniciar.setText("Volver a la hora real");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });
        pnlAdelantar.add(btnReiniciar);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlContenido.add(pnlAdelantar, gridBagConstraints);

        lblImpresoraTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblImpresoraTitulo.setText("Impresora");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 0, 0);
        pnlContenido.add(lblImpresoraTitulo, gridBagConstraints);

        pnlImpresora.setBackground(new java.awt.Color(255, 255, 255));
        pnlImpresora.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 2));

        rbPantalla.setText("Ventana en pantalla");
        rbPantalla.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        rbPantalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPantallaActionPerformed(evt);
            }
        });
        pnlImpresora.add(rbPantalla);

        rbArchivo.setText("Archivos .txt (carpeta impresiones)");
        rbArchivo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        rbArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbArchivoActionPerformed(evt);
            }
        });
        pnlImpresora.add(rbArchivo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlContenido.add(pnlImpresora, gridBagConstraints);

        lblUsuariosTitulo.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblUsuariosTitulo.setText("Usuarios de prueba");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 6, 0);
        pnlContenido.add(lblUsuariosTitulo, gridBagConstraints);

        scrUsuarios.setPreferredSize(new java.awt.Dimension(520, 120));
        txtUsuarios.setEditable(false);
        txtUsuarios.setColumns(40);
        txtUsuarios.setRows(5);
        txtUsuarios.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        scrUsuarios.setViewportView(txtUsuarios);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlContenido.add(scrUsuarios, gridBagConstraints);

        getContentPane().add(pnlContenido, java.awt.BorderLayout.CENTER);

        pnlBotones.setBackground(new java.awt.Color(244, 244, 245));
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCerrar);

        getContentPane().add(pnlBotones, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdelantarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdelantarActionPerformed
        int horas = (Integer) spnHoras.getValue();
        int minutos = (Integer) spnMinutos.getValue();
        gui.getApp().getReloj().adelantarMinutos(horas * 60L + minutos);
        actualizar();
    }//GEN-LAST:event_btnAdelantarActionPerformed

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        gui.getApp().getReloj().reiniciar();
        actualizar();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void rbPantallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPantallaActionPerformed
        gui.getApp().usarImpresoraArchivo(false);
        actualizar();
    }//GEN-LAST:event_rbPantallaActionPerformed

    private void rbArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbArchivoActionPerformed
        gui.getApp().usarImpresoraArchivo(true);
        actualizar();
    }//GEN-LAST:event_rbArchivoActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdelantar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JLabel lblHoras;
    private javax.swing.JLabel lblImpresoraTitulo;
    private javax.swing.JLabel lblMinutos;
    private javax.swing.JLabel lblReloj;
    private javax.swing.JLabel lblRelojNota;
    private javax.swing.JLabel lblRelojTitulo;
    private javax.swing.JLabel lblUsuariosTitulo;
    private javax.swing.JPanel pnlAdelantar;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlImpresora;
    private javax.swing.JRadioButton rbArchivo;
    private javax.swing.JRadioButton rbPantalla;
    private javax.swing.JScrollPane scrUsuarios;
    private javax.swing.JSpinner spnHoras;
    private javax.swing.JSpinner spnMinutos;
    private javax.swing.JTextArea txtUsuarios;
    // End of variables declaration//GEN-END:variables
}
