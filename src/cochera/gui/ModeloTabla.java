package cochera.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla GENÉRICO: sirve para mostrar cualquier lista de objetos (Pago, TicketCochera, Tarifa...).
 * Cada columna se define con una lambda que saca el valor a mostrar del objeto.
 *
 * <pre>
 * ModeloTabla&lt;Tarifa&gt; m = new ModeloTabla&lt;Tarifa&gt;()
 *         .columna("Tipo", t -&gt; t.getTipoVehiculo())
 *         .columna("Base", t -&gt; Dinero.soles(t.getCostoBase()));
 * </pre>
 */
public class ModeloTabla<T> extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    /** Cómo obtener el valor de una columna a partir de una fila. */
    @FunctionalInterface
    public interface Columna<T> {
        Object valor(T fila);
    }

    private final List<String> nombres = new ArrayList<>();
    private final List<Columna<T>> columnas = new ArrayList<>();
    private List<T> filas = new ArrayList<>();

    public ModeloTabla<T> columna(String nombre, Columna<T> columna) {
        nombres.add(nombre);
        columnas.add(columna);
        return this;
    }

    public void setFilas(List<T> nuevas) {
        filas = new ArrayList<>(nuevas);
        fireTableDataChanged();
    }

    /** Reemplaza las filas manteniendo seleccionado el mismo objeto (útil en vistas que se refrescan solas). */
    public void setFilasConservandoSeleccion(JTable tabla, List<T> nuevas) {
        T seleccionado = getSeleccionado(tabla);
        setFilas(nuevas);
        if (seleccionado != null) {
            int i = filas.indexOf(seleccionado);
            if (i >= 0) {
                tabla.setRowSelectionInterval(i, i);
            }
        }
    }

    public T getFila(int indice) {
        return filas.get(indice);
    }

    public List<T> getFilas() {
        return Collections.unmodifiableList(filas);
    }

    /** Objeto de la fila seleccionada en la tabla, o null. */
    public T getSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : filas.get(tabla.convertRowIndexToModel(fila));
    }

    @Override
    public int getRowCount() {
        return filas.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.size();
    }

    @Override
    public String getColumnName(int columna) {
        return nombres.get(columna);
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        return columnas.get(columna).valor(filas.get(fila));
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}
