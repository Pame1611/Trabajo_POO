package cochera.modelo;

/** Métodos de pago aceptados (enum metodo_pago). */
public enum MetodoPago {
    EFECTIVO("Efectivo"),
    YAPE("Yape"),
    PLIN("Plin");

    private final String etiqueta;

    MetodoPago(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
