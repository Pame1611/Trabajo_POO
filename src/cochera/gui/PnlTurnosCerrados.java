package cochera.gui;

import cochera.dto.PaginaResultado;
import cochera.dto.ReporteTurno;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.time.LocalDate;

/**
 * Pestaña "Turnos cerrados" (JPanel Form): auditoría del arqueo de cada cajero, paginada.
 */
public class PnlTurnosCerrados extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final ModeloTabla<ReporteTurno> modelo;
    private int pagina = 1;
    private transient PaginaResultado<ReporteTurno> resultado;

    public PnlTurnosCerrados(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        modelo = new ModeloTabla<ReporteTurno>()
                .columna("Empleado", r -> r.getTurno().getEmpleado().getNombreCompleto())
                .columna("Apertura", r -> FechaUtil.fechaHora(r.getTurno().getFechaApertura()))
                .columna("Cierre", r -> FechaUtil.fechaHora(r.getTurno().getFechaCierre()))
                .columna("Efectivo declarado", r -> Dinero.soles(r.getTurno().getEfectivoDeclarado()))
                .columna("Diferencia de caja", r -> Dinero.conSigno(r.getTurno().getDiferenciaCaja()))
                .columna("Total cobrado en el turno", r -> Dinero.soles(r.getRecaudado()));
        tblTurnos.setModel(modelo);
        Estilo.tabla(tblTurnos);
        Estilo.derecha(tblTurnos, 3, 4, 5);
        Estilo.botonPrimario(btnBuscar);
        Estilo.botonSecundario(btnAnterior);
        Estilo.botonSecundario(btnSiguiente);
    }

    @Override
    public void refrescar() {
        cargar();
    }

    private void cargar() {
        gui.ejecutar(this, () -> {
            LocalDate desde = UtilForm.fecha(txtDesde, "desde");
            LocalDate hasta = UtilForm.fecha(txtHasta, "hasta");
            resultado = gui.getApp().getAdmin().obtenerTurnosCerrados(desde, hasta, pagina);
            pagina = resultado.getPagina();
            modelo.setFilas(resultado.getDatos());
            lblPagina.setText("Página " + resultado.getPagina() + " de " + Math.max(1, resultado.getPaginas())
                    + "   (" + resultado.getTotal() + " turnos)");
            btnAnterior.setEnabled(resultado.hayAnterior());
            btnSiguiente.setEnabled(resultado.haySiguiente());
        });
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlTurnosCerrados.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlFiltro = new javax.swing.JPanel();
        lblDesde = new javax.swing.JLabel();
        txtDesde = new javax.swing.JTextField();
        lblHasta = new javax.swing.JLabel();
        txtHasta = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        lblNotaFechas = new javax.swing.JLabel();
        scrTurnos = new javax.swing.JScrollPane();
        tblTurnos = new javax.swing.JTable();
        pnlPaginas = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        btnSiguiente = new javax.swing.JButton();

        setBackground(new java.awt.Color(244, 244, 245));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setLayout(new java.awt.BorderLayout(0, 12));

        pnlFiltro.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltro.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        pnlFiltro.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 6));

        lblDesde.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblDesde.setText("Desde:");
        pnlFiltro.add(lblDesde);

        txtDesde.setColumns(9);
        txtDesde.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlFiltro.add(txtDesde);

        lblHasta.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblHasta.setText("Hasta:");
        pnlFiltro.add(lblHasta);

        txtHasta.setColumns(9);
        txtHasta.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlFiltro.add(txtHasta);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        pnlFiltro.add(btnBuscar);

        lblNotaFechas.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblNotaFechas.setForeground(new java.awt.Color(113, 113, 122));
        lblNotaFechas.setText("dd/mm/aaaa (vacío = sin límite)");
        pnlFiltro.add(lblNotaFechas);

        add(pnlFiltro, java.awt.BorderLayout.NORTH);

        scrTurnos.setViewportView(tblTurnos);

        add(scrTurnos, java.awt.BorderLayout.CENTER);

        pnlPaginas.setBackground(new java.awt.Color(244, 244, 245));
        pnlPaginas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));

        btnAnterior.setText("< Anterior");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        pnlPaginas.add(btnAnterior);

        lblPagina.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblPagina.setText("Página 1 de 1");
        pnlPaginas.add(lblPagina);

        btnSiguiente.setText("Siguiente >");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        pnlPaginas.add(btnSiguiente);

        add(pnlPaginas, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        pagina = 1;
        cargar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        pagina--;
        cargar();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        pagina++;
        cargar();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblNotaFechas;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlPaginas;
    private javax.swing.JScrollPane scrTurnos;
    private javax.swing.JTable tblTurnos;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables
}
