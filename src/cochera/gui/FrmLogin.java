package cochera.gui;

import cochera.excepcion.CocheraException;
import cochera.modelo.Usuario;

/**
 * Formulario de inicio de sesión (JFrame Form).
 */
public class FrmLogin extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;

    public FrmLogin(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        Estilo.botonPrimario(btnIngresar);
        Estilo.botonSecundario(btnDemo);
        getRootPane().setDefaultButton(btnIngresar);
        setSize(900, 540);
        setLocationRelativeTo(null);
    }

    /** Valida el usuario con el servicio de autenticación y abre la ventana según su rol. */
    private void ingresar() {
        lblError.setText(" ");
        try {
            Usuario usuario = gui.getApp().getAuth().iniciarSesion(txtCorreo.getText(), new String(txtClave.getPassword()));
            dispose();
            gui.getAplicacion().abrirSesion(usuario);
        } catch (CocheraException e) {
            lblError.setText(e.getMessage());
            txtClave.setText("");
            txtClave.requestFocusInWindow();
        }
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de FrmLogin.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMarca = new javax.swing.JPanel();
        lblMarca1 = new javax.swing.JLabel();
        lblMarca2 = new javax.swing.JLabel();
        lblLema = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        lblRelleno = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        pnlFormulario = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        lblClave = new javax.swing.JLabel();
        txtClave = new javax.swing.JPasswordField();
        lblError = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        lblDemoTitulo = new javax.swing.JLabel();
        lblDemo = new javax.swing.JLabel();
        btnDemo = new javax.swing.JButton();
        lblRelleno2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cochera Los Ángeles — Iniciar sesión");
        getContentPane().setLayout(new java.awt.BorderLayout());

        pnlMarca.setBackground(new java.awt.Color(24, 24, 27));
        pnlMarca.setBorder(javax.swing.BorderFactory.createEmptyBorder(48, 40, 32, 40));
        pnlMarca.setPreferredSize(new java.awt.Dimension(360, 480));
        pnlMarca.setLayout(new java.awt.GridBagLayout());

        lblMarca1.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        lblMarca1.setForeground(new java.awt.Color(255, 255, 255));
        lblMarca1.setText("COCHERA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMarca.add(lblMarca1, gridBagConstraints);

        lblMarca2.setFont(new java.awt.Font("Segoe UI", 1, 34)); // NOI18N
        lblMarca2.setForeground(new java.awt.Color(251, 191, 36));
        lblMarca2.setText("LOS ÁNGELES");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMarca.add(lblMarca2, gridBagConstraints);

        lblLema.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLema.setForeground(new java.awt.Color(161, 161, 170));
        lblLema.setText("Sistema de gestión de estacionamiento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlMarca.add(lblLema, gridBagConstraints);

        lblDireccion.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblDireccion.setForeground(new java.awt.Color(161, 161, 170));
        lblDireccion.setText("<html>Av. José Gabriel Condorcanqui 1413<br>La Esperanza · Trujillo</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 0, 0, 0);
        pnlMarca.add(lblDireccion, gridBagConstraints);

        lblRelleno.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblRelleno.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weighty = 1.0;
        pnlMarca.add(lblRelleno, gridBagConstraints);

        lblVersion.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblVersion.setForeground(new java.awt.Color(113, 113, 122));
        lblVersion.setText("Versión Java · Programación Orientada a Objetos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMarca.add(lblVersion, gridBagConstraints);

        getContentPane().add(pnlMarca, java.awt.BorderLayout.WEST);

        pnlFormulario.setBackground(new java.awt.Color(255, 255, 255));
        pnlFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(48, 56, 32, 56));
        pnlFormulario.setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(24, 24, 27));
        lblTitulo.setText("Iniciar sesión");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlFormulario.add(lblTitulo, gridBagConstraints);

        lblSubtitulo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitulo.setForeground(new java.awt.Color(113, 113, 122));
        lblSubtitulo.setText("Ingresa con tu correo y contraseña.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 22, 0);
        pnlFormulario.add(lblSubtitulo, gridBagConstraints);

        lblCorreo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCorreo.setText("Correo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlFormulario.add(lblCorreo, gridBagConstraints);

        txtCorreo.setColumns(24);
        txtCorreo.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlFormulario.add(txtCorreo, gridBagConstraints);

        lblClave.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblClave.setText("Contraseña");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        pnlFormulario.add(lblClave, gridBagConstraints);

        txtClave.setColumns(24);
        txtClave.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlFormulario.add(txtClave, gridBagConstraints);

        lblError.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblError.setForeground(new java.awt.Color(220, 38, 38));
        lblError.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlFormulario.add(lblError, gridBagConstraints);

        btnIngresar.setText("Ingresar");
        btnIngresar.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlFormulario.add(btnIngresar, gridBagConstraints);

        lblDemoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDemoTitulo.setForeground(new java.awt.Color(113, 113, 122));
        lblDemoTitulo.setText("USUARIOS DE PRUEBA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 0, 0, 0);
        pnlFormulario.add(lblDemoTitulo, gridBagConstraints);

        lblDemo.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblDemo.setForeground(new java.awt.Color(24, 24, 27));
        lblDemo.setText("<html>admin@cochera.pe  /  admin123<br>cajero1@cochera.pe  /  cajero123<br>cajero2@cochera.pe  /  cajero123</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlFormulario.add(lblDemo, gridBagConstraints);

        btnDemo.setText("Herramientas de demostración");
        btnDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDemoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlFormulario.add(btnDemo, gridBagConstraints);

        lblRelleno2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblRelleno2.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.weighty = 1.0;
        pnlFormulario.add(lblRelleno2, gridBagConstraints);

        getContentPane().add(pnlFormulario, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveActionPerformed
        ingresar();
    }//GEN-LAST:event_txtClaveActionPerformed

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        ingresar();
    }//GEN-LAST:event_btnIngresarActionPerformed

    private void btnDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDemoActionPerformed
        new DlgDemostracion(this, gui).setVisible(true);
    }//GEN-LAST:event_btnDemoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDemo;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel lblClave;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblDemo;
    private javax.swing.JLabel lblDemoTitulo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblLema;
    private javax.swing.JLabel lblMarca1;
    private javax.swing.JLabel lblMarca2;
    private javax.swing.JLabel lblRelleno;
    private javax.swing.JLabel lblRelleno2;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JPanel pnlMarca;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration//GEN-END:variables
}
