package cochera.servicio;

import cochera.datos.BaseDatos;
import cochera.excepcion.CocheraException;
import cochera.excepcion.ValidacionException;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.util.Placa;
import cochera.util.Reloj;
import java.util.List;

/**
 * Registro de ENTRADA de vehículos (app/actions/checkin.ts).
 */
public class CheckinServicio {

    private final BaseDatos bd;
    private final Reloj reloj;
    private final TurnoServicio turnos;

    public CheckinServicio(BaseDatos bd, Reloj reloj, TurnoServicio turnos) {
        this.bd = bd;
        this.reloj = reloj;
        this.turnos = turnos;
    }

    public List<Tarifa> obtenerTarifario() {
        return bd.listarTarifas();
    }

    /**
     * Flujo:
     *  1. Verifica turno ABIERTO.
     *  2. Normaliza placa (mayúsculas, sin guion).
     *  3. Valida el tipo de vehículo.
     *  4. Rechaza duplicados (misma placa ya EN_CURSO).
     *  5. Genera el correlativo "TK-AAAA-NNNNNN" e inserta, en una sola operación atómica.
     */
    public TicketCochera registrarEntrada(String placa, int idTarifa) throws CocheraException {
        turnos.requerirTurnoAbierto(
                "No tienes un turno abierto. Abre tu turno desde el Inicio antes de registrar entradas.");

        String placaNorm = Placa.normalizar(placa);
        if (placaNorm.isEmpty()) {
            throw new ValidacionException("La placa no puede estar vacía.");
        }
        if (placaNorm.length() < 3) {
            throw new ValidacionException("La placa ingresada es demasiado corta.");
        }
        if (idTarifa <= 0) {
            throw new ValidacionException("Selecciona un tipo de vehículo.");
        }
        Tarifa tarifa = bd.buscarTarifa(idTarifa);
        if (tarifa == null) {
            throw new ValidacionException("Tipo de vehículo no válido.");
        }

        TicketCochera duplicado = bd.buscarTicketEnCursoPorPlaca(placaNorm);
        if (duplicado != null) {
            throw new ValidacionException("Esta placa ya tiene un ticket activo (" + duplicado.getCodigoBarras()
                    + "). Realiza el check-out primero.");
        }

        return bd.registrarTicket(placaNorm, tarifa, reloj.ahora());
    }
}
