package cochera.gui;

import cochera.util.Placa;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Formatea la placa mientras se escribe: solo A-Z y 0-9, mayúsculas y guion automático
 * ("abc123" -> "ABC-123"). Equivale al hook usePlacaInput.ts de la web.
 */
public class FiltroPlaca extends DocumentFilter {

    public static void instalar(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new FiltroPlaca());
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attr) throws BadLocationException {
        replace(fb, offset, 0, texto, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int largo, String texto, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        String actual = doc.getText(0, doc.getLength());
        String nuevo = actual.substring(0, offset) + (texto == null ? "" : texto) + actual.substring(offset + largo);
        fb.replace(0, doc.getLength(), Placa.formatear(nuevo), attrs);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int largo) throws BadLocationException {
        Document doc = fb.getDocument();
        // Si se borra solo el guion, se borra también el carácter anterior (como en la web).
        if (largo == 1 && offset > 0 && "-".equals(doc.getText(offset, 1))) {
            replace(fb, offset - 1, 2, "", null);
        } else {
            replace(fb, offset, largo, "", null);
        }
    }
}
