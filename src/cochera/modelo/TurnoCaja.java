package cochera.modelo;

import cochera.util.Dinero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Turno de caja de un empleado (tabla turnos_caja).
 * Todo cobro queda atado al turno ABIERTO de quien cobra.
 */
public class TurnoCaja {

    private final String id;
    private final Empleado empleado;
    private final LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private EstadoTurno estado;
    private Double efectivoDeclarado;
    private Double diferenciaCaja;

    public TurnoCaja(Empleado empleado, LocalDateTime fechaApertura) {
        this.id = UUID.randomUUID().toString();
        this.empleado = empleado;
        this.fechaApertura = fechaApertura;
        this.estado = EstadoTurno.ABIERTO;
    }

    /**
     * Cierra el turno con el arqueo de caja.
     * diferencia = efectivo contado - efectivo que dice el sistema.
     * (Yape/Plin son informativos: no son dinero físico en caja.)
     */
    public void cerrar(LocalDateTime fechaCierre, double efectivoDeclarado, double efectivoSistema) {
        if (estado == EstadoTurno.CERRADO) {
            throw new IllegalStateException("El turno ya estaba cerrado.");
        }
        this.fechaCierre = fechaCierre;
        this.efectivoDeclarado = Dinero.redondear(efectivoDeclarado);
        this.diferenciaCaja = Dinero.redondear(efectivoDeclarado - efectivoSistema);
        this.estado = EstadoTurno.CERRADO;
    }

    public boolean estaAbierto() {
        return estado == EstadoTurno.ABIERTO;
    }

    public boolean perteneceA(Usuario usuario) {
        return usuario != null && empleado.getId().equals(usuario.getId());
    }

    public String getId() {
        return id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public Double getEfectivoDeclarado() {
        return efectivoDeclarado;
    }

    public Double getDiferenciaCaja() {
        return diferenciaCaja;
    }
}
