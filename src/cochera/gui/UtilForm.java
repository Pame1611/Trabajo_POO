package cochera.gui;

import cochera.excepcion.ValidacionException;
import cochera.util.FechaUtil;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Ayudas para leer y escuchar campos de texto de los formularios.
 */
public final class UtilForm {

    private UtilForm() {
    }

    /** Ejecuta la acción cada vez que cambia el texto del campo. */
    public static void alCambiar(JTextComponent campo, final Runnable accion) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                accion.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                accion.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                accion.run();
            }
        });
    }

    /** Fecha opcional: vacío = null. Acepta dd/mm/aaaa o aaaa-mm-dd. */
    public static LocalDate fecha(JTextField campo, String nombre) throws ValidacionException {
        String t = campo.getText().trim();
        if (t.isEmpty()) {
            return null;
        }
        try {
            return FechaUtil.parsearFecha(t);
        } catch (DateTimeParseException e) {
            throw new ValidacionException("La fecha \"" + nombre + "\" no es válida. Usa dd/mm/aaaa o aaaa-mm-dd.");
        }
    }

    /** Monto opcional: vacío = null. Acepta coma o punto decimal. */
    public static Double monto(String texto, String nombre) throws ValidacionException {
        String t = texto == null ? "" : texto.trim().replace(',', '.');
        if (t.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            throw new ValidacionException("El monto \"" + nombre + "\" no es válido. Ejemplo: 12.50");
        }
    }
}
