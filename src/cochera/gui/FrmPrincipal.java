package cochera.gui;

import cochera.modelo.Usuario;
import cochera.util.FechaUtil;
import java.awt.Component;

/**
 * Ventana principal (JFrame Form). Es el MISMO formulario para cajero y administrador:
 * el PerfilVentana recibido decide qué pestañas (JPanel Forms) se agregan.
 */
public class FrmPrincipal extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final transient PerfilVentana perfil;
    private final javax.swing.Timer temporizador;

    public FrmPrincipal(ContextoGui gui, PerfilVentana perfil) {
        this.gui = gui;
        this.perfil = perfil;
        initComponents();
        setTitle("Cochera Los Ángeles — " + perfil.getTitulo());
        lblSeccion.setText(perfil.getTitulo());
        Estilo.botonEncabezado(btnImpresora);
        Estilo.botonEncabezado(btnDemo);
        Estilo.botonPrimario(btnCerrarSesion);
        perfil.agregarPestanas(tabPrincipal, gui);
        tabPrincipal.addChangeListener(e -> refrescarPestanaActual());
        gui.setOyenteCambios(this::actualizarEncabezado);
        // Vista "en vivo": cada 5 segundos se actualiza la hora y los paneles que lo piden.
        temporizador = new javax.swing.Timer(5000, e -> alPasarTiempo());
        temporizador.start();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                salir();
            }
        });
        setSize(1280, 800);
        setMinimumSize(new java.awt.Dimension(1050, 660));
        setLocationRelativeTo(null);
        actualizarEncabezado();
        refrescarPestanaActual();
    }

    public final void actualizarEncabezado() {
        Usuario u = gui.getApp().getAuth().getUsuarioActual();
        lblUsuario.setText(u == null ? "" : u.getNombreCompleto() + "  ·  " + (u.esAdministrador() ? "Administrador" : "Cajero"));
        long desfase = gui.getApp().getReloj().getMinutosDesfase();
        lblHora.setText(FechaUtil.fechaHora(gui.getApp().getReloj().ahora())
                + (desfase > 0 ? "   (reloj adelantado " + FechaUtil.formatoMinutos(desfase) + ")" : ""));
        lblEstado.setText(perfil.textoEstado(gui));
        lblEstado.setBackground(perfil.colorEstado(gui));
        btnImpresora.setText(gui.getApp().getImpresora().estaConectada() ? "Impresora: conectada" : "Conectar impresora");
    }

    /** Polimorfismo por interfaz: cualquier pestaña Refrescable sabe actualizarse. */
    private void refrescarPestanaActual() {
        Component actual = tabPrincipal.getSelectedComponent();
        if (actual instanceof Refrescable) {
            ((Refrescable) actual).refrescar();
        }
    }

    private void alPasarTiempo() {
        actualizarEncabezado();
        Component actual = tabPrincipal.getSelectedComponent();
        if (actual instanceof Refrescable && ((Refrescable) actual).seActualizaSolo()) {
            ((Refrescable) actual).refrescar();
        }
    }

    private void salir() {
        if (Dialogos.confirmar(this, "¿Salir del sistema?\nLos datos de esta sesión están en memoria y se perderán.")) {
            temporizador.stop();
            System.exit(0);
        }
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de FrmPrincipal.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlEncabezado = new javax.swing.JPanel();
        lblMarca = new javax.swing.JLabel();
        lblSeccion = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        lblEspacio = new javax.swing.JLabel();
        pnlSesion = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        btnImpresora = new javax.swing.JButton();
        btnDemo = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cochera Los Ángeles");
        getContentPane().setLayout(new java.awt.BorderLayout());

        pnlEncabezado.setBackground(new java.awt.Color(24, 24, 27));
        pnlEncabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 18, 10, 18));
        pnlEncabezado.setLayout(new java.awt.GridBagLayout());

        lblMarca.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        lblMarca.setForeground(new java.awt.Color(255, 255, 255));
        lblMarca.setText("COCHERA LOS ÁNGELES");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEncabezado.add(lblMarca, gridBagConstraints);

        lblSeccion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSeccion.setForeground(new java.awt.Color(251, 191, 36));
        lblSeccion.setText("Caja");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        pnlEncabezado.add(lblSeccion, gridBagConstraints);

        lblEstado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setText("  ESTADO  ");
        lblEstado.setBackground(new java.awt.Color(5, 150, 105));
        lblEstado.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        pnlEncabezado.add(lblEstado, gridBagConstraints);

        lblEspacio.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblEspacio.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        pnlEncabezado.add(lblEspacio, gridBagConstraints);

        pnlSesion.setBackground(new java.awt.Color(24, 24, 27));
        pnlSesion.setLayout(new java.awt.GridLayout(2, 1, 0, 0));

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("Usuario");
        lblUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pnlSesion.add(lblUsuario);

        lblHora.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblHora.setForeground(new java.awt.Color(161, 161, 170));
        lblHora.setText("Hora");
        lblHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pnlSesion.add(lblHora);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlEncabezado.add(pnlSesion, gridBagConstraints);

        btnImpresora.setText("Conectar impresora");
        btnImpresora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpresoraActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        pnlEncabezado.add(btnImpresora, gridBagConstraints);

        btnDemo.setText("Demo");
        btnDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDemoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlEncabezado.add(btnDemo, gridBagConstraints);

        btnCerrarSesion.setText("Cerrar sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlEncabezado.add(btnCerrarSesion, gridBagConstraints);

        getContentPane().add(pnlEncabezado, java.awt.BorderLayout.NORTH);

        tabPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        getContentPane().add(tabPrincipal, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnImpresoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpresoraActionPerformed
        gui.ejecutar(this, () -> {
            if (gui.getApp().getImpresora().estaConectada()) {
                gui.getApp().getImpresora().desconectar();
            } else {
                gui.getApp().getImpresora().conectar();
            }
        });
    }//GEN-LAST:event_btnImpresoraActionPerformed

    private void btnDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDemoActionPerformed
        new DlgDemostracion(this, gui).setVisible(true);
        actualizarEncabezado();
        refrescarPestanaActual();
    }//GEN-LAST:event_btnDemoActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        if (Dialogos.confirmar(this, "¿Cerrar la sesión?")) {
            temporizador.stop();
            gui.getAplicacion().cerrarSesion(this);
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnDemo;
    private javax.swing.JButton btnImpresora;
    private javax.swing.JLabel lblEspacio;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblSeccion;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlSesion;
    private javax.swing.JTabbedPane tabPrincipal;
    // End of variables declaration//GEN-END:variables
}
