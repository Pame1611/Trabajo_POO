package cochera.gui;

import cochera.dto.FilaExport;
import cochera.excepcion.CocheraException;
import cochera.modelo.Tarifa;
import cochera.util.Dinero;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pestaña "Exportar a Excel" (JPanel Form): filtra las ventas y genera el CSV (reemplaza a SheetJS).
 */
public class PnlExportar extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private static final String TODOS = "Todos";

    private final transient ContextoGui gui;
    private final ModeloTabla<String[]> modelo = new ModeloTabla<>();

    public PnlExportar(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        for (int i = 0; i < FilaExport.CABECERAS.length; i++) {
            final int columna = i;
            modelo.columna(FilaExport.CABECERAS[i], fila -> fila[columna]);
        }
        tblVista.setModel(modelo);
        Estilo.tabla(tblVista);
        tblVista.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Estilo.anchos(tblVista, 130, 85, 75, 200, 125, 125, 95, 105, 80, 80, 95, 90, 80, 150);
        Estilo.derecha(tblVista, 7, 8, 9, 10);
        Estilo.botonSecundario(btnVistaPrevia);
        Estilo.botonPrimario(btnExportar);
        Estilo.botonSecundario(btnAbrirCarpeta);
        cboTipo.addItem(TODOS);
        for (Tarifa t : gui.getApp().getBaseDatos().listarTarifas()) {
            cboTipo.addItem(t.getTipoVehiculo());
        }
    }

    @Override
    public void refrescar() {
        Object elegido = cboEmpleado.getSelectedItem();
        cboEmpleado.removeAllItems();
        cboEmpleado.addItem(TODOS);
        try {
            for (String nombre : gui.getApp().getAdmin().obtenerNombresEmpleados()) {
                cboEmpleado.addItem(nombre);
            }
        } catch (CocheraException e) {
            Dialogos.error(this, e.getMessage());
        }
        if (elegido != null) {
            cboEmpleado.setSelectedItem(elegido);
        }
    }

    private List<FilaExport> obtenerFilas() throws CocheraException {
        String tipo = (String) cboTipo.getSelectedItem();
        String empleado = (String) cboEmpleado.getSelectedItem();
        return gui.getApp().getAdmin().obtenerFilasExport(
                UtilForm.fecha(txtDesde, "desde"), UtilForm.fecha(txtHasta, "hasta"),
                TODOS.equals(tipo) ? null : tipo, TODOS.equals(empleado) ? null : empleado);
    }

    private void mostrar(List<FilaExport> filas) {
        List<String[]> datos = new ArrayList<>();
        double total = 0;
        for (FilaExport f : filas) {
            datos.add(f.comoArreglo());
            total += f.getTotalCobrado();
        }
        modelo.setFilas(datos);
        lblResultado.setText(filas.size() + " venta(s)  ·  total " + Dinero.soles(total));
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlExportar.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlFiltros = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblDescripcion = new javax.swing.JLabel();
        lblDesde = new javax.swing.JLabel();
        txtDesde = new javax.swing.JTextField();
        lblHasta = new javax.swing.JLabel();
        txtHasta = new javax.swing.JTextField();
        lblTipo = new javax.swing.JLabel();
        cboTipo = new javax.swing.JComboBox<>();
        lblEmpleado = new javax.swing.JLabel();
        cboEmpleado = new javax.swing.JComboBox<>();
        pnlBotones = new javax.swing.JPanel();
        btnVistaPrevia = new javax.swing.JButton();
        lblSep1 = new javax.swing.JLabel();
        btnExportar = new javax.swing.JButton();
        lblSep2 = new javax.swing.JLabel();
        btnAbrirCarpeta = new javax.swing.JButton();
        lblResultado = new javax.swing.JLabel();
        scrVista = new javax.swing.JScrollPane();
        tblVista = new javax.swing.JTable();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.GridBagLayout());

        pnlFiltros.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlFiltros.setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitulo.setText("Exportar reporte de ventas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlFiltros.add(lblTitulo, gridBagConstraints);

        lblDescripcion.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(113, 113, 122));
        lblDescripcion.setText("Genera un archivo CSV que se abre directamente en Excel (una fila por cobro).");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 12, 0);
        pnlFiltros.add(lblDescripcion, gridBagConstraints);

        lblDesde.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDesde.setText("Desde (dd/mm/aaaa):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlFiltros.add(lblDesde, gridBagConstraints);

        txtDesde.setColumns(10);
        txtDesde.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 20);
        pnlFiltros.add(txtDesde, gridBagConstraints);

        lblHasta.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblHasta.setText("Hasta (dd/mm/aaaa):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlFiltros.add(lblHasta, gridBagConstraints);

        txtHasta.setColumns(10);
        txtHasta.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        pnlFiltros.add(txtHasta, gridBagConstraints);

        lblTipo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTipo.setText("Tipo de vehículo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlFiltros.add(lblTipo, gridBagConstraints);

        cboTipo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 20);
        pnlFiltros.add(cboTipo, gridBagConstraints);

        lblEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblEmpleado.setText("Empleado:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlFiltros.add(lblEmpleado, gridBagConstraints);

        cboEmpleado.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 8, 0, 0);
        pnlFiltros.add(cboEmpleado, gridBagConstraints);

        pnlBotones.setBackground(new java.awt.Color(255, 255, 255));
        pnlBotones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        btnVistaPrevia.setText("Vista previa");
        btnVistaPrevia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVistaPreviaActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVistaPrevia);

        lblSep1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSep1.setText("   ");
        pnlBotones.add(lblSep1);

        btnExportar.setText("Exportar a Excel (CSV)");
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnExportar);

        lblSep2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSep2.setText("   ");
        pnlBotones.add(lblSep2);

        btnAbrirCarpeta.setText("Abrir carpeta de reportes");
        btnAbrirCarpeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirCarpetaActionPerformed(evt);
            }
        });
        pnlBotones.add(btnAbrirCarpeta);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 0, 0, 0);
        pnlFiltros.add(pnlBotones, gridBagConstraints);

        lblResultado.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblResultado.setForeground(new java.awt.Color(5, 150, 105));
        lblResultado.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlFiltros.add(lblResultado, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pnlFiltros, gridBagConstraints);

        scrVista.setViewportView(tblVista);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(scrVista, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnVistaPreviaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVistaPreviaActionPerformed
        gui.ejecutar(this, () -> mostrar(obtenerFilas()));
    }//GEN-LAST:event_btnVistaPreviaActionPerformed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        gui.ejecutar(this, () -> {
            List<FilaExport> filas = obtenerFilas();
            mostrar(filas);
            if (filas.isEmpty()) {
                Dialogos.info(this, "No hay ventas con esos filtros.");
                return;
            }
            try {
                File archivo = gui.getApp().getExportador().exportarVentas(filas);
                lblResultado.setText(lblResultado.getText() + "  ·  guardado en " + archivo.getAbsolutePath());
                Dialogos.info(this, "Reporte generado (se abre con Excel):\n" + archivo.getAbsolutePath());
            } catch (IOException e) {
                Dialogos.error(this, "No se pudo exportar: " + e.getMessage());
            }
        });
    }//GEN-LAST:event_btnExportarActionPerformed

    private void btnAbrirCarpetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirCarpetaActionPerformed
        File carpeta = new File("reportes");
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            Dialogos.error(this, "No se pudo crear la carpeta " + carpeta.getAbsolutePath());
            return;
        }
        try {
            java.awt.Desktop.getDesktop().open(carpeta);
        } catch (Exception e) {
            Dialogos.info(this, "Los reportes están en:\n" + carpeta.getAbsolutePath());
        }
    }//GEN-LAST:event_btnAbrirCarpetaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirCarpeta;
    private javax.swing.JButton btnExportar;
    private javax.swing.JButton btnVistaPrevia;
    private javax.swing.JComboBox<String> cboEmpleado;
    private javax.swing.JComboBox<String> cboTipo;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblEmpleado;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblResultado;
    private javax.swing.JLabel lblSep1;
    private javax.swing.JLabel lblSep2;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JPanel pnlFiltros;
    private javax.swing.JScrollPane scrVista;
    private javax.swing.JTable tblVista;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables
}
