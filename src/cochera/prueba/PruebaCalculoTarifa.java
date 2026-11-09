package cochera.prueba;

import cochera.dto.ResultadoTarifa;
import cochera.servicio.CalculadoraTarifa;
import java.time.LocalDateTime;

/**
 * Casos límite de la regla de bloques de 12 h (checklist del proyecto: 11h59, 12h00, 12h01, 25h...).
 * Ejecutar: java -cp bin cochera.prueba.PruebaCalculoTarifa
 */
public class PruebaCalculoTarifa {

    public static void main(String[] args) {
        Verificador v = new Verificador();
        CalculadoraTarifa calc = new CalculadoraTarifa();
        LocalDateTime entrada = LocalDateTime.of(2026, 11, 2, 8, 0);

        System.out.println("=== Auto: tarifa base S/ 6.00, hora extra S/ 1.00 ===");
        // {minutos, total esperado}
        int[][] casosAuto = {
            {10 * 60, 6}, {11 * 60 + 59, 6}, {12 * 60, 6}, {12 * 60 + 1, 6},
            {12 * 60 + 30, 6}, {12 * 60 + 31, 7}, {15 * 60, 9}, {24 * 60, 12},
            {25 * 60, 13}, {26 * 60, 14}, {36 * 60, 18},
        };
        for (int[] c : casosAuto) {
            ResultadoTarifa r = calc.calcular(entrada, entrada.plusMinutes(c[0]), 6.00, 1.00);
            v.igual(c[1], r.getTarifaTotal(), (c[0] / 60) + "h" + String.format("%02d", c[0] % 60));
        }

        System.out.println("=== Tope: el sobrecargo nunca supera la tarifa base ===");
        ResultadoTarifa bici = calc.calcular(entrada, entrada.plusHours(18), 3.00, 1.00);
        v.igual(6.00, bici.getTarifaTotal(), "Bicicleta 18h: 3 + min(6 x 1, 3)");

        System.out.println("=== Camioneta: base S/ 8.00, hora extra S/ 1.50 ===");
        ResultadoTarifa cam = calc.calcular(entrada, entrada.plusMinutes(13 * 60 + 5), 8.00, 1.50);
        v.igual(11.00, cam.getTarifaTotal(), "13h05: 8 + 2 h extra x 1.50");
        v.verdadero(cam.getHorasExtra() == 2, "13h05 cobra 2 horas extra (fraccion = hora completa)");

        v.resumen();
        System.exit(v.getFallos() == 0 ? 0 : 1);
    }
}
