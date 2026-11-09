package cochera.dto;

/**
 * Resultado del cálculo de tarifa (interface ResultadoTarifa de lib/tarifa.ts).
 */
public class ResultadoTarifa {

    private final long minutos;
    private final long bloques;
    private final double tarifaTotal;
    private final int horasExtra;
    private final double montoSobrecargo;

    public ResultadoTarifa(long minutos, long bloques, double tarifaTotal, int horasExtra, double montoSobrecargo) {
        this.minutos = minutos;
        this.bloques = bloques;
        this.tarifaTotal = tarifaTotal;
        this.horasExtra = horasExtra;
        this.montoSobrecargo = montoSobrecargo;
    }

    /** Minutos totales de estadía (redondeado hacia abajo). */
    public long getMinutos() {
        return minutos;
    }

    /** Bloques de 12 h cobrados a tarifa base (mínimo 1). */
    public long getBloques() {
        return bloques;
    }

    public double getTarifaTotal() {
        return tarifaTotal;
    }

    /** Horas extra cobradas después de la gracia de 30 min. */
    public int getHorasExtra() {
        return horasExtra;
    }

    public double getMontoSobrecargo() {
        return montoSobrecargo;
    }
}
