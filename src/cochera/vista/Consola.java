package cochera.vista;

import cochera.util.FechaUtil;
import cochera.util.Texto;
import java.io.Console;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Entrada/salida por consola. Toda lectura valida el dato y vuelve a preguntar si es incorrecto.
 */
public class Consola {

    private static final int ANCHO = 78;

    private final Scanner entrada = new Scanner(System.in);

    public String leerTexto(String prompt) {
        System.out.print(prompt);
        if (!entrada.hasNextLine()) {
            throw new FinDeEntradaException();
        }
        return entrada.nextLine().trim();
    }

    public String leerObligatorio(String prompt) {
        while (true) {
            String valor = leerTexto(prompt);
            if (!valor.isEmpty()) {
                return valor;
            }
            error("Este dato es obligatorio.");
        }
    }

    /** Oculta la contraseña si se ejecuta en una terminal real; en el IDE se ve. */
    public String leerClave(String prompt) {
        Console c = System.console();
        if (c != null) {
            char[] clave = c.readPassword(prompt);
            if (clave == null) {
                throw new FinDeEntradaException();
            }
            return new String(clave);
        }
        return leerTexto(prompt);
    }

    public int leerEntero(String prompt, int min, int max) {
        while (true) {
            String valor = leerTexto(prompt);
            try {
                int n = Integer.parseInt(valor);
                if (n >= min && n <= max) {
                    return n;
                }
            } catch (NumberFormatException e) {
                // se vuelve a preguntar
            }
            error("Ingresa un numero entre " + min + " y " + max + ".");
        }
    }

    /** Devuelve null si se deja vacío. Acepta "12.50" o "12,50". */
    public Double leerDecimalOpcional(String prompt) {
        while (true) {
            String valor = leerTexto(prompt).replace(',', '.');
            if (valor.isEmpty()) {
                return null;
            }
            try {
                return Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                error("Monto invalido. Ejemplo: 12.50");
            }
        }
    }

    public double leerDecimal(String prompt) {
        while (true) {
            Double valor = leerDecimalOpcional(prompt);
            if (valor != null) {
                return valor;
            }
            error("Este dato es obligatorio.");
        }
    }

    /** Devuelve null si se deja vacío. Acepta 2026-11-02 o 02/11/2026. */
    public LocalDate leerFechaOpcional(String prompt) {
        while (true) {
            String valor = leerTexto(prompt);
            if (valor.isEmpty()) {
                return null;
            }
            try {
                return FechaUtil.parsearFecha(valor);
            } catch (DateTimeParseException e) {
                error("Fecha invalida. Usa dd/mm/aaaa o aaaa-mm-dd.");
            }
        }
    }

    public boolean confirmar(String prompt) {
        while (true) {
            String valor = leerTexto(prompt + " (s/n): ").toLowerCase();
            if (valor.equals("s") || valor.equals("si")) {
                return true;
            }
            if (valor.equals("n") || valor.equals("no")) {
                return false;
            }
            error("Responde s o n.");
        }
    }

    public void titulo(String texto) {
        System.out.println();
        System.out.println(Texto.repetir('=', ANCHO));
        System.out.println(Texto.centrar(texto.toUpperCase(), ANCHO));
        System.out.println(Texto.repetir('=', ANCHO));
    }

    public void subtitulo(String texto) {
        System.out.println();
        System.out.println("--- " + texto + " " + Texto.repetir('-', Math.max(0, ANCHO - texto.length() - 5)));
    }

    public void linea() {
        System.out.println(Texto.repetir('-', ANCHO));
    }

    public void info(String texto) {
        System.out.println(texto);
    }

    public void exito(String texto) {
        System.out.println("[OK] " + texto);
    }

    public void aviso(String texto) {
        System.out.println("[AVISO] " + texto);
    }

    public void error(String texto) {
        System.out.println("[ERROR] " + texto);
    }

    public void pausa() {
        leerTexto("Presiona ENTER para continuar...");
    }
}
