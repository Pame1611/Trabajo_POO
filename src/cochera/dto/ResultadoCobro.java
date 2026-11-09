package cochera.dto;

import cochera.modelo.Pago;

/**
 * Resultado del check-out: el pago registrado, la tarifa aplicada y el vuelto (si fue en efectivo).
 */
public class ResultadoCobro {

    private final Pago pago;
    private final ResultadoTarifa tarifa;
    private final double montoRecibido;
    private final double vuelto;

    public ResultadoCobro(Pago pago, ResultadoTarifa tarifa, double montoRecibido, double vuelto) {
        this.pago = pago;
        this.tarifa = tarifa;
        this.montoRecibido = montoRecibido;
        this.vuelto = vuelto;
    }

    public Pago getPago() {
        return pago;
    }

    public ResultadoTarifa getTarifa() {
        return tarifa;
    }

    public double getMontoRecibido() {
        return montoRecibido;
    }

    public double getVuelto() {
        return vuelto;
    }
}
