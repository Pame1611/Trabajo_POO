package cochera.dto;

import cochera.modelo.Pago;
import java.util.List;

/**
 * KPIs del día para el panel del administrador (KpiHoy en admin.ts).
 */
public class KpiHoy {

    private final int autosDentro;
    private final ResumenPagos resumen;
    private final double[] ingresosPorHora;
    private final List<MontoPorTipo> porTipo;
    private final List<Pago> registrosRecientes;

    public KpiHoy(int autosDentro, ResumenPagos resumen, double[] ingresosPorHora,
                  List<MontoPorTipo> porTipo, List<Pago> registrosRecientes) {
        this.autosDentro = autosDentro;
        this.resumen = resumen;
        this.ingresosPorHora = ingresosPorHora.clone();
        this.porTipo = porTipo;
        this.registrosRecientes = registrosRecientes;
    }

    public int getAutosDentro() {
        return autosDentro;
    }

    public double getIngresosHoy() {
        return resumen.getTotal();
    }

    public int getTicketsHoy() {
        return resumen.getCantidad();
    }

    public ResumenPagos getResumen() {
        return resumen;
    }

    /** 24 posiciones (hora 0-23 de Lima) con la suma cobrada en esa hora. */
    public double[] getIngresosPorHora() {
        return ingresosPorHora.clone();
    }

    public List<MontoPorTipo> getPorTipo() {
        return porTipo;
    }

    /** Últimos 10 cobros del día. */
    public List<Pago> getRegistrosRecientes() {
        return registrosRecientes;
    }
}
