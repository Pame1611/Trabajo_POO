package cochera.modelo;

import cochera.util.Dinero;

/**
 * Fila del tarifario: tipo de vehículo, costo por bloque de 12 h y sobrecargo por hora extra.
 */
public class Tarifa {

    private final int id;
    private final String tipoVehiculo;
    private double costoBase;
    private double sobrecargoHora;

    public Tarifa(int id, String tipoVehiculo, double costoBase, double sobrecargoHora) {
        this.id = id;
        this.tipoVehiculo = tipoVehiculo;
        actualizarPrecios(costoBase, sobrecargoHora);
    }

    /** Solo se permite cambiar precios, no crear ni borrar categorías. */
    public final void actualizarPrecios(double nuevoCostoBase, double nuevoSobrecargo) {
        if (nuevoCostoBase < 0 || nuevoSobrecargo < 0) {
            throw new IllegalArgumentException("Los valores deben ser >= 0.");
        }
        this.costoBase = Dinero.redondear(nuevoCostoBase);
        this.sobrecargoHora = Dinero.redondear(nuevoSobrecargo);
    }

    public int getId() {
        return id;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public double getSobrecargoHora() {
        return sobrecargoHora;
    }

    @Override
    public String toString() {
        return tipoVehiculo + " (" + Dinero.soles(costoBase) + " / 12h, +" + Dinero.soles(sobrecargoHora) + " x h extra)";
    }
}
