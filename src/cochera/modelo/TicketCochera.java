package cochera.modelo;

import cochera.util.Placa;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Estadía de un vehículo (tabla tickets_cochera).
 * Nace EN_CURSO en el check-in y pasa a PAGADO en el check-out (o ANULADO por el admin).
 */
public class TicketCochera {

    private final String id;
    private final String placa;
    private final Tarifa tarifa;
    private final LocalDateTime horaEntrada;
    private final String codigoBarras;
    private LocalDateTime horaSalida;
    private EstadoTicket estado;

    public TicketCochera(String placa, Tarifa tarifa, LocalDateTime horaEntrada, String codigoBarras) {
        this.id = UUID.randomUUID().toString();
        this.placa = placa;
        this.tarifa = tarifa;
        this.horaEntrada = horaEntrada;
        this.codigoBarras = codigoBarras;
        this.estado = EstadoTicket.EN_CURSO;
    }

    /** Check-out: registra la salida y marca el ticket como PAGADO. */
    public void registrarSalida(LocalDateTime salida) {
        if (!estaEnCurso()) {
            throw new IllegalStateException("El ticket " + codigoBarras + " no esta EN_CURSO.");
        }
        this.horaSalida = salida;
        this.estado = EstadoTicket.PAGADO;
    }

    /** Anulación administrativa: el pago queda como registro histórico. */
    public void anular() {
        this.estado = EstadoTicket.ANULADO;
    }

    public boolean estaEnCurso() {
        return estado == EstadoTicket.EN_CURSO;
    }

    public String getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getPlacaFormateada() {
        return Placa.formatear(placa);
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public EstadoTicket getEstado() {
        return estado;
    }
}
