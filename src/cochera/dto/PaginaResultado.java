package cochera.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado paginado GENÉRICO: sirve para cualquier tipo T (turnos, pagos...).
 */
public class PaginaResultado<T> {

    private final List<T> datos;
    private final int total;
    private final int pagina;
    private final int paginas;

    public PaginaResultado(List<T> datos, int total, int pagina, int paginas) {
        this.datos = datos;
        this.total = total;
        this.pagina = pagina;
        this.paginas = paginas;
    }

    /** Corta la página solicitada (1, 2, 3...) de una lista completa. */
    public static <T> PaginaResultado<T> paginar(List<T> todos, int pagina, int tamanio) {
        int paginas = (int) Math.ceil(todos.size() / (double) tamanio);
        int actual = Math.max(1, Math.min(pagina, Math.max(1, paginas)));
        int desde = (actual - 1) * tamanio;
        int hasta = Math.min(todos.size(), desde + tamanio);
        List<T> corte = desde < hasta ? new ArrayList<>(todos.subList(desde, hasta)) : new ArrayList<T>();
        return new PaginaResultado<>(Collections.unmodifiableList(corte), todos.size(), actual, paginas);
    }

    public List<T> getDatos() {
        return datos;
    }

    public int getTotal() {
        return total;
    }

    public int getPagina() {
        return pagina;
    }

    public int getPaginas() {
        return paginas;
    }

    public boolean haySiguiente() {
        return pagina < paginas;
    }

    public boolean hayAnterior() {
        return pagina > 1;
    }
}
