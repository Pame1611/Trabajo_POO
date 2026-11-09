package cochera.gui;

import cochera.dto.CotizacionSalida;
import cochera.dto.ResultadoCobro;
import cochera.dto.ResultadoTarifa;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.impresion.TicketSalida;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.TicketCochera;
import cochera.util.Dinero;
import cochera.util.FechaUtil;
import java.util.List;

/**
 * Pestaña "Check-out" (JPanel Form): busca el ticket, muestra la tarifa por bloques de 12 h,
 * calcula el vuelto y registra el pago en el turno de quien cobra.
 */
public class PnlCheckout extends javax.swing.JPanel implements Refrescable {

    private static final long serialVersionUID = 1L;

    private final transient ContextoGui gui;
    private final javax.swing.ButtonGroup grupoMetodo = new javax.swing.ButtonGroup();
    private transient CotizacionSalida cotizacion;

    public PnlCheckout(ContextoGui gui) {
        this.gui = gui;
        initComponents();
        grupoMetodo.add(rbEfectivo);
        grupoMetodo.add(rbYape);
        grupoMetodo.add(rbPlin);
        Estilo.botonPrimario(btnBuscar);
        Estilo.botonPrimario(btnCobrar);
        Estilo.botonSecundario(btnCancelar);
        UtilForm.alCambiar(txtRecibido, this::actualizarVuelto);
        limpiar();
    }

    @Override
    public void refrescar() {
        boolean hayTurno = gui.getApp().getTurnos().obtenerTurnoAbierto() != null;
        lblAviso.setVisible(!hayTurno);
        txtBuscar.setEnabled(hayTurno);
        btnBuscar.setEnabled(hayTurno);
        btnCobrar.setEnabled(hayTurno);
        if (hayTurno && cotizacion == null) {
            txtBuscar.requestFocusInWindow();
        }
    }

    /** Usado desde "Autos dentro": carga el ticket elegido sin volver a escribir la placa. */
    public void cargar(String codigoBarras) {
        txtBuscar.setText(codigoBarras);
        buscar();
    }

    private void buscar() {
        lblMensaje.setText(" ");
        String entrada = txtBuscar.getText().trim();
        try {
            cotizacion = gui.getApp().getCheckout().buscar(entrada);
            mostrarCotizacion();
        } catch (ValidacionException e) {
            String mensaje = e.getMessage();
            if (!entrada.toUpperCase().startsWith("TK-")) {
                try {
                    List<String> sugerencias = gui.getApp().getCheckout().sugerirPlacas(entrada);
                    if (!sugerencias.isEmpty()) {
                        mensaje += "   ¿Quisiste decir: " + String.join(", ", sugerencias) + "?";
                    }
                } catch (CocheraException ignorada) {
                    // sin sugerencias
                }
            }
            lblMensaje.setText(mensaje);
            ocultarCobro();
        } catch (CocheraException e) {
            Dialogos.error(this, e.getMessage());
        }
    }

    private void mostrarCotizacion() {
        TicketCochera t = cotizacion.getTicket();
        ResultadoTarifa r = cotizacion.getTarifa();
        lblVTicket.setText(t.getCodigoBarras());
        lblVPlaca.setText(t.getPlacaFormateada());
        lblVVehiculo.setText(t.getTarifa().getTipoVehiculo());
        lblVIngreso.setText(FechaUtil.fechaHora(t.getHoraEntrada()));
        lblVTiempo.setText(FechaUtil.formatoMinutos(r.getMinutos()));
        lblVBase.setText(Dinero.soles(t.getTarifa().getCostoBase()) + " x " + r.getBloques() + " bloque(s) de 12 h");
        if (r.getHorasExtra() > 0) {
            lblVExtra.setText(r.getHorasExtra() + " x " + Dinero.soles(t.getTarifa().getSobrecargoHora()) + " = "
                    + Dinero.soles(r.getMontoSobrecargo()));
        } else {
            lblVExtra.setText(r.getMinutos() >= 12 * 60 ? "Ninguna (gracia de 30 min)" : "Ninguna");
        }
        lblTotal.setText(Dinero.soles(r.getTarifaTotal()));
        btnCobrar.setText("Cobrar " + Dinero.soles(r.getTarifaTotal()));
        rbEfectivo.setSelected(true);
        txtRecibido.setText("");
        txtRecibido.setEnabled(true);
        actualizarVuelto();
        pnlDetalle.setVisible(true);
        pnlPago.setVisible(true);
        revalidate();
        txtRecibido.requestFocusInWindow();
    }

    private MetodoPago metodoElegido() {
        return rbYape.isSelected() ? MetodoPago.YAPE : rbPlin.isSelected() ? MetodoPago.PLIN : MetodoPago.EFECTIVO;
    }

    private void actualizarVuelto() {
        if (cotizacion == null || metodoElegido() != MetodoPago.EFECTIVO) {
            lblVuelto.setText(" ");
            return;
        }
        try {
            Double recibido = UtilForm.monto(txtRecibido.getText(), "recibido");
            if (recibido == null) {
                lblVuelto.setText("Vuelto: S/ 0.00 (monto exacto)");
                lblVuelto.setForeground(Estilo.TEXTO_SUAVE);
            } else {
                double vuelto = gui.getApp().getCheckout().calcularVuelto(cotizacion.getTarifa().getTarifaTotal(), recibido);
                lblVuelto.setText("Vuelto: " + Dinero.soles(vuelto));
                lblVuelto.setForeground(Estilo.VERDE);
            }
        } catch (CocheraException e) {
            lblVuelto.setText(e.getMessage().startsWith("El monto recibido") ? "Monto insuficiente" : "Monto inválido");
            lblVuelto.setForeground(Estilo.ROJO);
        }
    }

    private void cobrar() {
        if (cotizacion == null) {
            return;
        }
        gui.ejecutar(this, () -> {
            MetodoPago metodo = metodoElegido();
            Double recibido = metodo == MetodoPago.EFECTIVO ? UtilForm.monto(txtRecibido.getText(), "recibido") : null;
            double total = cotizacion.getTarifa().getTarifaTotal();
            if (!Dialogos.confirmar(this, "¿Estás seguro de haber recibido el pago de " + Dinero.soles(total)
                    + " (" + metodo + ")?")) {
                return;
            }
            ResultadoCobro r = gui.getApp().getCheckout().registrarPago(cotizacion.getTicket(), metodo, recibido);
            Pago pago = r.getPago();
            StringBuilder msg = new StringBuilder("Cobro registrado: " + Dinero.soles(pago.getMontoTotal()) + " (" + metodo + ").\n"
                    + pago.getTicket().getPlacaFormateada() + " puede salir.");
            if (r.getTarifa().getTarifaTotal() != total) {
                msg.append("\n\nLa tarifa se recalculó al momento exacto del cobro.");
            }
            if (metodo == MetodoPago.EFECTIVO && recibido != null) {
                msg.append("\n\nRecibido: ").append(Dinero.soles(r.getMontoRecibido()))
                        .append("\nVuelto: ").append(Dinero.soles(r.getVuelto()));
            }
            limpiar();
            Dialogos.info(this, msg.toString());
            gui.imprimir(this, new TicketSalida(pago));
        });
    }

    private void ocultarCobro() {
        cotizacion = null;
        pnlDetalle.setVisible(false);
        pnlPago.setVisible(false);
        revalidate();
        repaint();
    }

    private void limpiar() {
        txtBuscar.setText("");
        lblMensaje.setText(" ");
        ocultarCobro();
        txtBuscar.requestFocusInWindow();
    }

    /**
     * Código generado por el editor visual de NetBeans a partir de PnlCheckout.form.
     * NO modificar a mano: se edita desde la vista Design.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblAviso = new javax.swing.JLabel();
        pnlBuscar = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblInstruccion = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        lblMensaje = new javax.swing.JLabel();
        pnlDetalle = new javax.swing.JPanel();
        lblDTicket = new javax.swing.JLabel();
        lblVTicket = new javax.swing.JLabel();
        lblDPlaca = new javax.swing.JLabel();
        lblVPlaca = new javax.swing.JLabel();
        lblDVehiculo = new javax.swing.JLabel();
        lblVVehiculo = new javax.swing.JLabel();
        lblDIngreso = new javax.swing.JLabel();
        lblVIngreso = new javax.swing.JLabel();
        lblDTiempo = new javax.swing.JLabel();
        lblVTiempo = new javax.swing.JLabel();
        lblDBase = new javax.swing.JLabel();
        lblVBase = new javax.swing.JLabel();
        lblDExtra = new javax.swing.JLabel();
        lblVExtra = new javax.swing.JLabel();
        lblTotalTitulo = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        pnlPago = new javax.swing.JPanel();
        lblPagoTitulo = new javax.swing.JLabel();
        rbEfectivo = new javax.swing.JRadioButton();
        rbYape = new javax.swing.JRadioButton();
        rbPlin = new javax.swing.JRadioButton();
        lblRecibido = new javax.swing.JLabel();
        txtRecibido = new javax.swing.JTextField();
        lblVuelto = new javax.swing.JLabel();
        btnCobrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
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

        pnlBuscar.setBackground(new java.awt.Color(255, 255, 255));
        pnlBuscar.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlBuscar.setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitulo.setText("Cobrar salida");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlBuscar.add(lblTitulo, gridBagConstraints);

        lblInstruccion.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblInstruccion.setForeground(new java.awt.Color(113, 113, 122));
        lblInstruccion.setText("Escanea el código de barras del ticket (TK-...) o escribe la placa y presiona Enter.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 10, 0);
        pnlBuscar.add(lblInstruccion, gridBagConstraints);

        txtBuscar.setColumns(20);
        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlBuscar.add(txtBuscar, gridBagConstraints);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlBuscar.add(btnBuscar, gridBagConstraints);

        lblMensaje.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblMensaje.setForeground(new java.awt.Color(220, 38, 38));
        lblMensaje.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        pnlBuscar.add(lblMensaje, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(pnlBuscar, gridBagConstraints);

        pnlDetalle.setBackground(new java.awt.Color(255, 255, 255));
        pnlDetalle.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlDetalle.setLayout(new java.awt.GridBagLayout());

        lblDTicket.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDTicket.setForeground(new java.awt.Color(113, 113, 122));
        lblDTicket.setText("Ticket");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDTicket, gridBagConstraints);

        lblVTicket.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVTicket.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVTicket, gridBagConstraints);

        lblDPlaca.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDPlaca.setForeground(new java.awt.Color(113, 113, 122));
        lblDPlaca.setText("Placa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDPlaca, gridBagConstraints);

        lblVPlaca.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVPlaca.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVPlaca, gridBagConstraints);

        lblDVehiculo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDVehiculo.setForeground(new java.awt.Color(113, 113, 122));
        lblDVehiculo.setText("Vehículo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDVehiculo, gridBagConstraints);

        lblVVehiculo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVVehiculo.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVVehiculo, gridBagConstraints);

        lblDIngreso.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDIngreso.setForeground(new java.awt.Color(113, 113, 122));
        lblDIngreso.setText("Ingreso");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDIngreso, gridBagConstraints);

        lblVIngreso.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVIngreso.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVIngreso, gridBagConstraints);

        lblDTiempo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDTiempo.setForeground(new java.awt.Color(113, 113, 122));
        lblDTiempo.setText("Tiempo dentro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDTiempo, gridBagConstraints);

        lblVTiempo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVTiempo.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVTiempo, gridBagConstraints);

        lblDBase.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDBase.setForeground(new java.awt.Color(113, 113, 122));
        lblDBase.setText("Tarifa base");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDBase, gridBagConstraints);

        lblVBase.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVBase.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVBase, gridBagConstraints);

        lblDExtra.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDExtra.setForeground(new java.awt.Color(113, 113, 122));
        lblDExtra.setText("Horas extra");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 18);
        pnlDetalle.add(lblDExtra, gridBagConstraints);

        lblVExtra.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblVExtra.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        pnlDetalle.add(lblVExtra, gridBagConstraints);

        lblTotalTitulo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalTitulo.setForeground(new java.awt.Color(113, 113, 122));
        lblTotalTitulo.setText("TOTAL A COBRAR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        pnlDetalle.add(lblTotalTitulo, gridBagConstraints);

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(180, 83, 9));
        lblTotal.setText("S/ 0.00");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        pnlDetalle.add(lblTotal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(pnlDetalle, gridBagConstraints);

        pnlPago.setBackground(new java.awt.Color(255, 255, 255));
        pnlPago.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(228, 228, 231)), javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        pnlPago.setLayout(new java.awt.GridBagLayout());

        lblPagoTitulo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblPagoTitulo.setText("Forma de pago");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlPago.add(lblPagoTitulo, gridBagConstraints);

        rbEfectivo.setText("Efectivo");
        rbEfectivo.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        rbEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbEfectivoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 16);
        pnlPago.add(rbEfectivo, gridBagConstraints);

        rbYape.setText("Yape");
        rbYape.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        rbYape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbYapeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 16);
        pnlPago.add(rbYape, gridBagConstraints);

        rbPlin.setText("Plin");
        rbPlin.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        rbPlin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPlinActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 16);
        pnlPago.add(rbPlin, gridBagConstraints);

        lblRecibido.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblRecibido.setText("Monto recibido (S/):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlPago.add(lblRecibido, gridBagConstraints);

        txtRecibido.setColumns(8);
        txtRecibido.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        pnlPago.add(txtRecibido, gridBagConstraints);

        lblVuelto.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblVuelto.setForeground(new java.awt.Color(113, 113, 122));
        lblVuelto.setText("Vuelto: —");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 8, 0, 0);
        pnlPago.add(lblVuelto, gridBagConstraints);

        btnCobrar.setText("Cobrar");
        btnCobrar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 0, 0);
        pnlPago.add(btnCobrar, gridBagConstraints);

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 0, 0, 0);
        pnlPago.add(btnCancelar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(pnlPago, gridBagConstraints);

        lblRelleno.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblRelleno.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weighty = 1.0;
        add(lblRelleno, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void rbEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbEfectivoActionPerformed
        txtRecibido.setEnabled(true);
        actualizarVuelto();
    }//GEN-LAST:event_rbEfectivoActionPerformed

    private void rbYapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbYapeActionPerformed
        txtRecibido.setText("");
        txtRecibido.setEnabled(false);
        actualizarVuelto();
    }//GEN-LAST:event_rbYapeActionPerformed

    private void rbPlinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPlinActionPerformed
        txtRecibido.setText("");
        txtRecibido.setEnabled(false);
        actualizarVuelto();
    }//GEN-LAST:event_rbPlinActionPerformed

    private void btnCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobrarActionPerformed
        cobrar();
    }//GEN-LAST:event_btnCobrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCobrar;
    private javax.swing.JLabel lblAviso;
    private javax.swing.JLabel lblDBase;
    private javax.swing.JLabel lblDExtra;
    private javax.swing.JLabel lblDIngreso;
    private javax.swing.JLabel lblDPlaca;
    private javax.swing.JLabel lblDTicket;
    private javax.swing.JLabel lblDTiempo;
    private javax.swing.JLabel lblDVehiculo;
    private javax.swing.JLabel lblInstruccion;
    private javax.swing.JLabel lblMensaje;
    private javax.swing.JLabel lblPagoTitulo;
    private javax.swing.JLabel lblRecibido;
    private javax.swing.JLabel lblRelleno;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalTitulo;
    private javax.swing.JLabel lblVBase;
    private javax.swing.JLabel lblVExtra;
    private javax.swing.JLabel lblVIngreso;
    private javax.swing.JLabel lblVPlaca;
    private javax.swing.JLabel lblVTicket;
    private javax.swing.JLabel lblVTiempo;
    private javax.swing.JLabel lblVVehiculo;
    private javax.swing.JLabel lblVuelto;
    private javax.swing.JPanel pnlBuscar;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlPago;
    private javax.swing.JRadioButton rbEfectivo;
    private javax.swing.JRadioButton rbPlin;
    private javax.swing.JRadioButton rbYape;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtRecibido;
    // End of variables declaration//GEN-END:variables
}
