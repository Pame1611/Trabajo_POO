package cochera.dto;

import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.util.Dinero;
import java.util.List;

/**
 * Totales de un conjunto de pagos agrupados por método (ResumenPagosTurno en turno.ts).
 */
public class ResumenPagos {

    private double efectivo;
    private double yape;
    private double plin;
    private int cantidad;

    public static ResumenPagos de(List<Pago> pagos) {
        ResumenPagos r = new ResumenPagos();
        for (Pago p : pagos) {
            r.sumar(p.getMetodoPago(), p.getMontoTotal());
        }
        return r;
    }

    public void sumar(MetodoPago metodo, double monto) {
        cantidad++;
        switch (metodo) {
            case EFECTIVO:
                efectivo += monto;
                break;
            case YAPE:
                yape += monto;
                break;
            case PLIN:
                plin += monto;
                break;
            default:
                break;
        }
    }

    public double getPorMetodo(MetodoPago metodo) {
        switch (metodo) {
            case EFECTIVO:
                return getEfectivo();
            case YAPE:
                return getYape();
            default:
                return getPlin();
        }
    }

    public double getEfectivo() {
        return Dinero.redondear(efectivo);
    }

    public double getYape() {
        return Dinero.redondear(yape);
    }

    public double getPlin() {
        return Dinero.redondear(plin);
    }

    public double getTotal() {
        return Dinero.redondear(efectivo + yape + plin);
    }

    public int getCantidad() {
        return cantidad;
    }
}
