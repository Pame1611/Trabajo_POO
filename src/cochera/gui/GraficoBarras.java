package cochera.gui;

import cochera.util.Dinero;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * Gráfico de barras de ingresos por hora (0 a 23 h), dibujado con Java2D.
 */
public class GraficoBarras extends JComponent {

    private static final long serialVersionUID = 1L;

    private double[] valores = new double[24];

    public GraficoBarras() {
        setPreferredSize(new Dimension(420, 220));
    }

    public void setValores(double[] nuevos) {
        valores = nuevos.clone();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int ancho = getWidth();
        int alto = getHeight();
        int margenIzq = 8;
        int margenInf = 22;
        int margenSup = 18;
        int areaAlto = alto - margenInf - margenSup;
        double maximo = 0;
        for (double v : valores) {
            maximo = Math.max(maximo, v);
        }

        g2.setColor(Estilo.BORDE);
        g2.drawLine(margenIzq, alto - margenInf, ancho - 4, alto - margenInf);

        Font chica = getFont().deriveFont(Font.PLAIN, 10f);
        g2.setFont(chica);
        FontMetrics fm = g2.getFontMetrics();
        double anchoBarra = (ancho - margenIzq - 6) / 24.0;
        for (int h = 0; h < 24; h++) {
            int x = (int) Math.round(margenIzq + h * anchoBarra + anchoBarra * 0.18);
            int w = Math.max(2, (int) Math.round(anchoBarra * 0.64));
            if (valores[h] > 0 && maximo > 0) {
                int barra = (int) Math.round(valores[h] / maximo * areaAlto);
                g2.setColor(Estilo.PRIMARIO);
                g2.fillRoundRect(x, alto - margenInf - barra, w, barra, 4, 4);
                String etiqueta = Dinero.decimal(valores[h]);
                g2.setColor(Estilo.TEXTO);
                g2.drawString(etiqueta, x + w / 2 - fm.stringWidth(etiqueta) / 2, alto - margenInf - barra - 3);
            }
            if (h % 3 == 0) {
                String hora = (h < 10 ? "0" : "") + h + "h";
                g2.setColor(Estilo.TEXTO_SUAVE);
                g2.drawString(hora, x + w / 2 - fm.stringWidth(hora) / 2, alto - 6);
            }
        }
        if (maximo == 0) {
            String vacio = "Aún no hay cobros hoy";
            g2.setColor(Estilo.TEXTO_SUAVE);
            g2.setFont(getFont().deriveFont(Font.PLAIN, 13f));
            fm = g2.getFontMetrics();
            g2.drawString(vacio, ancho / 2 - fm.stringWidth(vacio) / 2, alto / 2);
        }
        g2.dispose();
    }
}
