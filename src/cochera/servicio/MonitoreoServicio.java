package cochera.servicio;

import cochera.datos.BaseDatos;
import cochera.excepcion.AccesoDenegadoException;
import cochera.excepcion.CocheraException;
import cochera.modelo.Tarifa;
import cochera.modelo.TicketCochera;
import cochera.modelo.Usuario;
import cochera.util.FechaUtil;
import cochera.util.Placa;
import cochera.util.Reloj;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Inventario en vivo de vehículos dentro de la cochera (app/autos-dentro).
 */
public class MonitoreoServicio {

    private final BaseDatos bd;
    private final Reloj reloj;
    private final AutenticacionServicio auth;
    private final TurnoServicio turnos;
    private final CalculadoraTarifa calculadora;

    public MonitoreoServicio(BaseDatos bd, Reloj reloj, AutenticacionServicio auth, TurnoServicio turnos,
                             CalculadoraTarifa calculadora) {
        this.bd = bd;
        this.reloj = reloj;
        this.auth = auth;
        this.turnos = turnos;
        this.calculadora = calculadora;
    }

    /** ADMIN ve siempre; el EMPLEADO necesita turno abierto. Ordenado del ingreso más reciente al más antiguo. */
    public List<TicketCochera> listarAutosDentro() throws CocheraException {
        Usuario u = auth.requerirSesion();
        if (!u.esAdministrador() && turnos.obtenerTurnoAbierto() == null) {
            throw new AccesoDenegadoException("Debes abrir tu turno para ver los autos dentro.");
        }
        List<TicketCochera> lista = bd.listarTicketsEnCurso();
        lista.sort(Comparator.comparing(TicketCochera::getHoraEntrada).reversed());
        return lista;
    }

    /** Filtra por texto de placa y/o por tipo de vehículo (null = todos). */
    public List<TicketCochera> filtrar(List<TicketCochera> lista, String textoPlaca, Tarifa tipo) {
        String f = Placa.normalizar(textoPlaca);
        List<TicketCochera> resultado = new ArrayList<>();
        for (TicketCochera t : lista) {
            boolean coincidePlaca = f.isEmpty() || t.getPlaca().contains(f);
            boolean coincideTipo = tipo == null || t.getTarifa().getId() == tipo.getId();
            if (coincidePlaca && coincideTipo) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public String tiempoTranscurrido(TicketCochera t) {
        return FechaUtil.tiempoTranscurrido(t.getHoraEntrada(), reloj.ahora());
    }

    public NivelAlerta nivelAlerta(TicketCochera t) {
        double horas = Duration.between(t.getHoraEntrada(), reloj.ahora()).getSeconds() / 3600.0;
        return NivelAlerta.segunHoras(horas);
    }

    /** Lo que pagaría si saliera en este momento. */
    public double montoEstimado(TicketCochera t) {
        return calculadora.calcular(t, reloj.ahora()).getTarifaTotal();
    }
}
