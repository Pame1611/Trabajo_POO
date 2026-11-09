package cochera.dto;

/**
 * Recaudación acumulada de un tipo de vehículo.
 */
public class MontoPorTipo {

    private final String tipoVehiculo;
    private final double monto;

    public MontoPorTipo(String tipoVehiculo, double monto) {
        this.tipoVehiculo = tipoVehiculo;
        this.monto = monto;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public double getMonto() {
        return monto;
    }
}
