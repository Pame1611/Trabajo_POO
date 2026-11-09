package cochera.gui;

/**
 * "Papel" que sale de la impresora térmica simulada (JDialog Form). Lo usa ImpresoraVentana.
 */
public class DlgTicket extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    public DlgTicket(java.awt.Window duenio) {
        super(duenio, java.awt.Dialog.ModalityType.MODELESS);
        initComponents();
        Estilo.botonSecundario(btnCerrar);
        getContentPane().setBackground(Estilo.FONDO);
    }

    /** Muestra el texto impreso, a la derecha de la ventana que imprimió. */
    public void mostrar(String contenido) {
        txtTicket.setText(contenido);
        txtTicket.setCaretPosition(0);
        pack();
        setSize(getWidth(), Math.min(getHeight(), 680));
        java.awt.Window duenio = getOwner();
        if (duenio != null && duenio.isShowing()) {
            setLocation(duenio.getX() + duenio.getWidth() - getWidth() - 40, duenio.getY() + 70);
        } else {
            setLocationRelativeTo(null);
        }
        setVisible(true);
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de DlgTicket.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        scrTicket = new javax.swing.JScrollPane();
        txtTicket = new javax.swing.JTextArea();
        pnlBotones = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Impresora térmica 58 mm");
        getContentPane().setLayout(new java.awt.BorderLayout());

        scrTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 0, 14));
        scrTicket.setBackground(new java.awt.Color(244, 244, 245));
        txtTicket.setEditable(false);
        txtTicket.setColumns(34);
        txtTicket.setRows(30);
        txtTicket.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        txtTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        scrTicket.setViewportView(txtTicket);

        getContentPane().add(scrTicket, java.awt.BorderLayout.CENTER);

        pnlBotones.setBackground(new java.awt.Color(244, 244, 245));
        pnlBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 14, 10, 14));
        pnlBotones.setLayout(new java.awt.BorderLayout());

        lblInfo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(113, 113, 122));
        lblInfo.setText("Simulación de impresión (papel de 58 mm)");
        pnlBotones.add(lblInfo, java.awt.BorderLayout.CENTER);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnCerrar, java.awt.BorderLayout.EAST);

        getContentPane().add(pnlBotones, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JScrollPane scrTicket;
    private javax.swing.JTextArea txtTicket;
    // End of variables declaration//GEN-END:variables
}
