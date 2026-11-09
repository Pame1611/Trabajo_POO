package cochera.servicio;

/**
 * Semáforo de tiempo en "Autos dentro": avisa cuando un vehículo se acerca o pasa las 12 h.
 */
public enum NivelAlerta {
    NORMAL("OK"),
    POR_VENCER("POR VENCER"),
    EXCEDIDO("+12H");

    private final String etiqueta;

    NivelAlerta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public static NivelAlerta segunHoras(double horas) {
        if (horas >= 12) {
            return EXCEDIDO;
        }
        if (horas >= 10) {
            return POR_VENCER;
        }
        return NORMAL;
    }
}
