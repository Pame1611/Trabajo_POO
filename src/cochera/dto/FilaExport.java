package cochera.dto;

import cochera.util.Dinero;

/**
 * Fila del reporte exportable a Excel (FilaExport en admin.ts, sin campos de facturación).
 */
public class FilaExport {

    public static final String[] CABECERAS = {
        "Codigo Ticket", "Fecha", "Placa", "Tipo Vehiculo", "Hora Entrada", "Hora Salida",
        "Tiempo Estadia", "Tiempo Total (min)", "Tarifa Base", "Sobrecargo", "Total Cobrado",
        "Metodo Pago", "Estado", "Empleado"
    };

    private final String codigoTicket;
    private final String fecha;
    private final String placa;
    private final String tipoVehiculo;
    private final String horaEntrada;
    private final String horaSalida;
    private final String tiempoEstadia;
    private final long tiempoTotalMin;
    private final double tarifaBase;
    private final double sobrecargo;
    private final double totalCobrado;
    private final String metodoPago;
    private final String estado;
    private final String empleado;

    public FilaExport(String codigoTicket, String fecha, String placa, String tipoVehiculo, String horaEntrada,
                      String horaSalida, String tiempoEstadia, long tiempoTotalMin, double tarifaBase,
                      double sobrecargo, double totalCobrado, String metodoPago, String estado, String empleado) {
        this.codigoTicket = codigoTicket;
        this.fecha = fecha;
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.tiempoEstadia = tiempoEstadia;
        this.tiempoTotalMin = tiempoTotalMin;
        this.tarifaBase = tarifaBase;
        this.sobrecargo = sobrecargo;
        this.totalCobrado = totalCobrado;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.empleado = empleado;
    }

    public String[] comoArreglo() {
        return new String[] {
            codigoTicket, fecha, placa, tipoVehiculo, horaEntrada, horaSalida, tiempoEstadia,
            String.valueOf(tiempoTotalMin), Dinero.decimal(tarifaBase), Dinero.decimal(sobrecargo),
            Dinero.decimal(totalCobrado), metodoPago, estado, empleado
        };
    }

    public double getTotalCobrado() {
        return totalCobrado;
    }

    public String getPlaca() {
        return placa;
    }

    public String getEmpleado() {
        return empleado;
    }
}
