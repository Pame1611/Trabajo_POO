package cochera.gui;

import cochera.dto.PaginaResultado;
import cochera.impresion.TicketSalida;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.time.LocalDate;

/**
 * Pestaña "Pagos" del administrador (JPanel Form): todos los cobros, con reimpresión,
 * corrección del método de pago y anulación del ticket.
 */
public class PnlPagos extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final ModeloTabla<Pago> modelo;
    private int pagina = 1;

    public PnlPagos(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        modelo = new ModeloTabla<Pago>()
                .columna("Fecha y hora", p -> FechaUtil.fechaHora(p.getCreadoEn()))
                .columna("Código", p -> p.getTicket().getCodigoBarras())
                .columna("Placa", p -> p.getTicket().getPlacaFormateada())
                .columna("Tipo de vehículo", p -> p.getTicket().getTarifa().getTipoVehiculo())
                .columna("Monto", p -> Dinero.soles(p.getMontoTotal()))
                .columna("Método", Pago::getMetodoPago)
                .columna("Cobrado por", p -> p.getTurno().getEmpleado().getNombreCompleto())
                .columna("Ticket", p -> p.getTicket().getEstado());
        tblPagos.setModel(modelo);
        Estilo.tabla(tblPagos);
        Estilo.anchos(tblPagos, 140, 140, 90, 210, 90, 90, 160, 90);
        Estilo.derecha(tblPagos, 4);
        Estilo.botonPrimario(btnBuscar);
        Estilo.botonSecundario(btnAnterior);
        Estilo.botonSecundario(btnSiguiente);
        Estilo.botonSecundario(btnReimprimir);
        Estilo.botonSecundario(btnCambiarMetodo);
        Estilo.botonPeligro(btnAnular);
    }

    @Override
    public void refrescar() {
        cargar();
    }

    private void cargar() {
        gui.ejecutar(this, () -> {
            LocalDate desde = UtilForm.fecha(txtDesde, "desde");
            LocalDate hasta = UtilForm.fecha(txtHasta, "hasta");
            PaginaResultado<Pago> r = gui.getApp().getAdmin().obtenerPagos(desde, hasta, pagina);
            pagina = r.getPagina();
            modelo.setFilasConservandoSeleccion(tblPagos, r.getDatos());
            lblPagina.setText("Página " + r.getPagina() + " de " + Math.max(1, r.getPaginas()) + "   (" + r.getTotal() + " pagos)");
            btnAnterior.setEnabled(r.hayAnterior());
            btnSiguiente.setEnabled(r.haySiguiente());
        });
    }

    private Pago seleccionado() {
        Pago p = modelo.getSeleccionado(tblPagos);
        if (p == null) {
            Dialogos.info(this, "Selecciona un pago de la tabla.");
        }
        return p;
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlPagos.form.
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
        scrPagos = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        pnlSur = new javax.swing.JPanel();
        pnlPaginas = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        lblPagina = new javax.swing.JLabel();
        btnSiguiente = new javax.swing.JButton();
        pnlAcciones = new javax.swing.JPanel();
        btnReimprimir = new javax.swing.JButton();
        btnCambiarMetodo = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();

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

        scrPagos.setViewportView(tblPagos);

        add(scrPagos, java.awt.BorderLayout.CENTER);

        pnlSur.setBackground(new java.awt.Color(244, 244, 245));
        pnlSur.setLayout(new java.awt.BorderLayout());

        pnlPaginas.setBackground(new java.awt.Color(244, 244, 245));
        pnlPaginas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));

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

        pnlSur.add(pnlPaginas, java.awt.BorderLayout.WEST);

        pnlAcciones.setBackground(new java.awt.Color(244, 244, 245));
        pnlAcciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 8, 0));

        btnReimprimir.setText("Reimprimir ticket");
        btnReimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReimprimirActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnReimprimir);

        btnCambiarMetodo.setText("Cambiar método de pago");
        btnCambiarMetodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarMetodoActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnCambiarMetodo);

        btnAnular.setText("Anular ticket");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });
        pnlAcciones.add(btnAnular);

        pnlSur.add(pnlAcciones, java.awt.BorderLayout.EAST);

        add(pnlSur, java.awt.BorderLayout.SOUTH);
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

    private void btnReimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReimprimirActionPerformed
        Pago p = seleccionado();
        if (p != null) {
            gui.imprimir(this, new TicketSalida(p));
        }
    }//GEN-LAST:event_btnReimprimirActionPerformed

    private void btnCambiarMetodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarMetodoActionPerformed
        final Pago p = seleccionado();
        if (p == null) {
            return;
        }
        final MetodoPago nuevo = Dialogos.elegirMetodoPago(this, p.getMetodoPago());
        if (nuevo != null && gui.ejecutar(this, () -> gui.getApp().getCobros().cambiarMetodoPago(p, nuevo))) {
            Dialogos.info(this, "Método de pago actualizado a " + nuevo + ".");
        }
        cargar();
    }//GEN-LAST:event_btnCambiarMetodoActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        final Pago p = seleccionado();
        if (p != null && Dialogos.confirmar(this, "¿Anular el ticket " + p.getTicket().getCodigoBarras()
                + "?\nEl pago queda como registro histórico.")) {
            if (gui.ejecutar(this, () -> gui.getApp().getAdmin().anularTicket(p.getTicket()))) {
                Dialogos.info(this, "Ticket anulado.");
            }
            cargar();
        }
    }//GEN-LAST:event_btnAnularActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCambiarMetodo;
    private javax.swing.JButton btnReimprimir;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel lblDesde;
    private javax.swing.JLabel lblHasta;
    private javax.swing.JLabel lblNotaFechas;
    private javax.swing.JLabel lblPagina;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlPaginas;
    private javax.swing.JPanel pnlSur;
    private javax.swing.JScrollPane scrPagos;
    private javax.swing.JTable tblPagos;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables
}
