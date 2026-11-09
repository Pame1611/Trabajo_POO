package cochera.datos;

import cochera.excepcion.ValidacionException;
import cochera.modelo.Administrador;
import cochera.modelo.Empleado;
import cochera.modelo.MetodoPago;
import cochera.modelo.Pago;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.modelo.TurnoCaja;
import cochera.servicio.CalculadoraTarifa;
import cochera.util.Reloj;
import java.time.LocalDateTime;

/**
 * Carga inicial de datos: tarifario real (schema.sql, sección 5),
 * usuarios de prueba y, opcionalmente, movimientos de demostración.
 */
public final class DatosIniciales {

    public static final String CLAVE_DEMO_ADMIN = "admin123";
    public static final String CLAVE_DEMO_CAJERO = "cajero123";

    private DatosIniciales() {
    }

    public static void cargar(BaseDatos bd, Reloj reloj) {
        cargarTarifario(bd);
        LocalDateTime ahora = reloj.ahora();
        try {
            bd.insertarUsuario(new Administrador("admin@cochera.pe", CLAVE_DEMO_ADMIN, "Administrador General", ahora));
            bd.insertarUsuario(new Empleado("cajero1@cochera.pe", CLAVE_DEMO_CAJERO, "Carlos Ramírez", ahora));
            bd.insertarUsuario(new Empleado("cajero2@cochera.pe", CLAVE_DEMO_CAJERO, "Maria Torres", ahora));
        } catch (ValidacionException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void cargarTarifario(BaseDatos bd) {
        Object[][] filas = {
            {"Bicicleta", 3.00, 1.00},
            {"Triciclo", 4.00, 1.00},
            {"Moto Lineal", 4.00, 1.00},
            {"Carretilla", 5.00, 1.00},
            {"Mototaxi", 5.00, 1.00},
            {"Auto", 6.00, 1.00},
            {"Furgo Pequeño", 8.00, 1.00},
            {"Camioneta Cerrada y Abierta", 8.00, 1.50},
            {"Minivan Pequeña", 9.00, 1.50},
            {"Furgon Grande", 10.00, 1.50},
            {"Minivan Grande", 10.00, 1.50},
            {"Camion 1 Eje", 12.00, 1.50},
            {"Micro", 12.00, 1.50},
            {"Maquinaria Pequeña", 12.00, 1.50},
            {"Omnibus", 13.00, 1.50},
            {"Mini Bus", 13.00, 1.50},
            {"Camion 2 Ejes", 15.00, 1.50},
            {"Maquinaria Grande", 15.00, 1.50},
            {"Volquete", 15.00, 1.50},
            {"Omnibus 2 Pisos", 20.00, 1.50},
            {"Trailer Sin Carreta", 20.00, 1.50},
            {"Trailer Con Carreta", 30.00, 1.50},
        };
        for (int i = 0; i < filas.length; i++) {
            bd.insertarTarifa(new Tarifa(i + 1, (String) filas[i][0], (Double) filas[i][1], (Double) filas[i][2]));
        }
    }

    /**
     * Datos para la exposición: un turno cerrado de ayer con cobros y
     * 4 vehículos dentro con tiempos que muestran los casos de la regla de 12 h.
     */
    public static void cargarDemostracion(BaseDatos bd, Reloj reloj) {
        LocalDateTime ahora = reloj.ahora();
        CalculadoraTarifa calc = new CalculadoraTarifa();
        Empleado maria = (Empleado) bd.buscarUsuarioPorEmail("cajero2@cochera.pe");
        try {
            // ── Turno cerrado de ayer (María) con 4 cobros ──
            LocalDateTime apertura = ahora.minusDays(1).withHour(7).withMinute(0).withSecond(0);
            TurnoCaja turnoAyer = new TurnoCaja(maria, apertura);
            bd.insertarTurno(turnoAyer);

            // {placa, id tarifa, horas de estadía, hora de salida (desde la apertura), método}
            Object[][] historicos = {
                {"T7Y274", 6, 3, 4, MetodoPago.EFECTIVO},   // Auto
                {"AJH908", 17, 13, 6, MetodoPago.YAPE},     // Camion 2 ejes (entró la noche anterior)
                {"Z1I490", 3, 2, 8, MetodoPago.EFECTIVO},   // Moto lineal
                {"T8R822", 8, 9, 10, MetodoPago.PLIN},      // Camioneta
            };
            double efectivo = 0;
            for (Object[] h : historicos) {
                LocalDateTime salida = apertura.plusHours((Integer) h[3]);
                LocalDateTime entrada = salida.minusHours((Integer) h[2]).minusMinutes(10);
                Tarifa tarifa = bd.buscarTarifa((Integer) h[1]);
                TicketCochera t = new TicketCochera((String) h[0], tarifa, entrada,
                        bd.generarCorrelativoTicket(entrada.getYear()));
                bd.insertarTicket(t);
                t.registrarSalida(salida);
                double monto = calc.calcular(entrada, salida, tarifa.getCostoBase(), tarifa.getSobrecargoHora()).getTarifaTotal();
                Pago p = new Pago(t, turnoAyer, monto, (MetodoPago) h[4], salida);
                bd.insertarPago(p);
                if (h[4] == MetodoPago.EFECTIVO) {
                    efectivo += monto;
                }
            }
            turnoAyer.cerrar(apertura.plusHours(12), efectivo - 1.0, efectivo); // faltó S/ 1.00

            // ── Vehículos que siguen dentro ──
            Object[][] dentro = {
                {"ABC123", 6, 120},   // Auto, 2 h            -> tarifa base
                {"XYZ789", 8, 710},   // Camioneta, 11 h 50 m -> tarifa base
                {"BUS321", 15, 740},  // Omnibus, 12 h 20 m   -> gracia de 30 min
                {"MTR456", 3, 785},   // Moto lineal, 13 h 05 m -> base + horas extra
            };
            for (Object[] d : dentro) {
                LocalDateTime entrada = ahora.minusMinutes((Integer) d[2]);
                TicketCochera t = new TicketCochera((String) d[0], bd.buscarTarifa((Integer) d[1]), entrada,
                        bd.generarCorrelativoTicket(entrada.getYear()));
                bd.insertarTicket(t);
            }
        } catch (ValidacionException e) {
            throw new IllegalStateException(e);
        }
    }
}
