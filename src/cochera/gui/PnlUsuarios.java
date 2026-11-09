package cochera.gui;

import cochera.modelo.Empleado;
import cochera.modelo.Usuario;
import cochera.util.FechaUtil;

/**
 * Pestaña "Usuarios" (JPanel Form): lista de usuarios y alta de nuevos cajeros.
 */
public class PnlUsuarios extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final ModeloTabla<Usuario> modelo;

    public PnlUsuarios(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        modelo = new ModeloTabla<Usuario>()
                .columna("Nombre", Usuario::getNombreCompleto)
                .columna("Correo", Usuario::getEmail)
                .columna("Rol", u -> u.esAdministrador() ? "Administrador" : "Cajero (EMPLEADO)")
                .columna("Creado", u -> FechaUtil.fechaHora(u.getCreadoEn()));
        tblUsuarios.setModel(modelo);
        Estilo.tabla(tblUsuarios);
        Estilo.botonPrimario(btnRegistrar);
    }

    @Override
    public void refrescar() {
        gui.ejecutar(this, () -> modelo.setFilas(gui.getApp().getAuth().listarUsuarios()));
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlUsuarios.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrUsuarios = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        pnlNuevo = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblClave = new javax.swing.JLabel();
        txtClave = new javax.swing.JPasswordField();
        btnRegistrar = new javax.swing.JButton();
        lblNota = new javax.swing.JLabel();
        lblRelleno = new javax.swing.JLabel();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(12, 0));

        scrUsuarios.setViewportView(tblUsuarios);

        add(scrUsuarios, java.awt.BorderLayout.CENTER);

        pnlNuevo.setBackground(new java.awt.Color(255, 255, 255));
        pnlNuevo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlNuevo.setPreferredSize(new java.awt.Dimension(330, 100));
        pnlNuevo.setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        lblTitulo.setText("Registrar nuevo cajero");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlNuevo.add(lblTitulo, gridBagConstraints);

        lblCorreo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCorreo.setText("Correo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlNuevo.add(lblCorreo, gridBagConstraints);

        txtCorreo.setColumns(18);
        txtCorreo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlNuevo.add(txtCorreo, gridBagConstraints);

        lblNombre.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblNombre.setText("Nombre completo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlNuevo.add(lblNombre, gridBagConstraints);

        txtNombre.setColumns(18);
        txtNombre.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlNuevo.add(txtNombre, gridBagConstraints);

        lblClave.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblClave.setText("Contraseña (mínimo 6 caracteres)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlNuevo.add(lblClave, gridBagConstraints);

        txtClave.setColumns(18);
        txtClave.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlNuevo.add(txtClave, gridBagConstraints);

        btnRegistrar.setText("Registrar empleado");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlNuevo.add(btnRegistrar, gridBagConstraints);

        lblNota.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblNota.setForeground(new java.awt.Color(113, 113, 122));
        lblNota.setText("<html>Todo usuario nuevo se crea con rol EMPLEADO (igual que el trigger de la base de datos web).</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlNuevo.add(lblNota, gridBagConstraints);

        lblRelleno.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblRelleno.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.weighty = 1.0;
        pnlNuevo.add(lblRelleno, gridBagConstraints);

        add(pnlNuevo, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        final String correo = txtCorreo.getText();
        final String nombre = txtNombre.getText();
        final String clave = new String(txtClave.getPassword());
        if (gui.ejecutar(this, () -> {
            Empleado nuevo = gui.getApp().getAuth().registrarEmpleado(correo, clave, nombre);
            Dialogos.info(this, "Empleado registrado: " + nuevo.getNombreCompleto() + "\nYa puede iniciar sesión con " + nuevo.getEmail());
        })) {
            txtCorreo.setText("");
            txtNombre.setText("");
            txtClave.setText("");
            refrescar();
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel lblClave;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNota;
    private javax.swing.JLabel lblRelleno;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlNuevo;
    private javax.swing.JScrollPane scrUsuarios;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
