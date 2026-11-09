package cochera.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Colores y estilos comunes de la interfaz (mismos tonos que la versión web: zinc + ámbar).
 * Los formularios definen su diseño en la vista Design; aquí solo va lo que el editor
 * visual no permite (botones planos, tablas a rayas, colores de alerta).
 */
public final class Estilo {

    public static final Color PRIMARIO = new Color(180, 83, 9);
    public static final Color OSCURO = new Color(24, 24, 27);
    public static final Color FONDO = new Color(244, 244, 245);
    public static final Color BORDE = new Color(228, 228, 231);
    public static final Color TEXTO = new Color(24, 24, 27);
    public static final Color TEXTO_SUAVE = new Color(113, 113, 122);
    public static final Color VERDE = new Color(5, 150, 105);
    public static final Color AMBAR = new Color(217, 119, 6);
    public static final Color ROJO = new Color(220, 38, 38);
    private static final Color GRIS_BOTON = new Color(228, 228, 231);
    private static final Color GRIS_OSCURO = new Color(63, 63, 70);
    private static final Color FILA_PAR = new Color(250, 250, 250);
    private static final Color SELECCION = new Color(254, 243, 199);

    private Estilo() {
    }

    /** Nimbus (incluido en Java) con selección en tono ámbar. */
    public static void instalarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("nimbusSelectionBackground", PRIMARIO);
            UIManager.put("nimbusFocus", new Color(251, 191, 36));
        } catch (Exception e) {
            // Si Nimbus no está disponible se usa el aspecto por defecto.
        }
    }

    // ─────────────────────────── botones ───────────────────────────

    public static void botonPrimario(AbstractButton b) {
        plano(b, PRIMARIO, Color.WHITE);
    }

    public static void botonSecundario(AbstractButton b) {
        plano(b, GRIS_BOTON, TEXTO);
    }

    public static void botonPeligro(AbstractButton b) {
        plano(b, ROJO, Color.WHITE);
    }

    /** Botón para el encabezado oscuro. */
    public static void botonEncabezado(AbstractButton b) {
        plano(b, GRIS_OSCURO, Color.WHITE);
    }

    private static void plano(final AbstractButton b, final Color fondo, Color texto) {
        b.setUI(new BasicButtonUI());
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setForeground(texto);
        b.setBackground(fondo);
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.getModel().addChangeListener(e -> {
            ButtonModel m = b.getModel();
            if (!b.isEnabled()) {
                b.setBackground(mezclar(fondo, Color.WHITE, 0.55));
            } else if (m.isPressed()) {
                b.setBackground(mezclar(fondo, Color.BLACK, 0.25));
            } else if (m.isRollover()) {
                b.setBackground(mezclar(fondo, Color.BLACK, 0.12));
            } else {
                b.setBackground(fondo);
            }
        });
    }

    private static Color mezclar(Color a, Color b, double peso) {
        return new Color(
                (int) Math.round(a.getRed() * (1 - peso) + b.getRed() * peso),
                (int) Math.round(a.getGreen() * (1 - peso) + b.getGreen() * peso),
                (int) Math.round(a.getBlue() * (1 - peso) + b.getBlue() * peso));
    }

    // ─────────────────────────── tablas ───────────────────────────

    /** Filas a rayas, altura cómoda, una sola fila seleccionable. */
    public static void tabla(JTable t) {
        t.setRowHeight(28);
        t.setFillsViewportHeight(true);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setReorderingAllowed(false);
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t.setDefaultRenderer(Object.class, new Celda(SwingConstants.LEFT));
    }

    public static void anchos(JTable t, int... anchos) {
        for (int i = 0; i < anchos.length && i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    /** Columnas de montos alineadas a la derecha. */
    public static void derecha(JTable t, int... columnas) {
        for (int c : columnas) {
            t.getColumnModel().getColumn(c).setCellRenderer(new Celda(SwingConstants.RIGHT));
        }
    }

    /** Colorea la columna de alerta de "Autos dentro": OK verde, POR VENCER ámbar, +12H rojo. */
    public static void alerta(JTable t, int columna) {
        t.getColumnModel().getColumn(columna).setCellRenderer(new Celda(SwingConstants.CENTER) {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean sel, boolean foco, int f, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tabla, valor, sel, foco, f, c);
                String v = String.valueOf(valor);
                l.setForeground(v.startsWith("+") ? ROJO : v.startsWith("POR") ? AMBAR : VERDE);
                l.setFont(l.getFont().deriveFont(Font.BOLD));
                return l;
            }
        });
    }

    /** Renderizador de celda con filas a rayas y selección suave. */
    private static class Celda extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        Celda(int alineacion) {
            setHorizontalAlignment(alineacion);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean sel, boolean foco, int fila, int col) {
            super.getTableCellRendererComponent(tabla, valor, sel, false, fila, col);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            setForeground(TEXTO);
            setBackground(sel ? SELECCION : (fila % 2 == 0 ? Color.WHITE : FILA_PAR));
            return this;
        }
    }
}
