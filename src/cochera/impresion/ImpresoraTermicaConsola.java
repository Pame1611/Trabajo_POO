package cochera.impresion;

import cochera.excepcion.CocheraException;
import cochera.util.Texto;

/**
 * Simula la térmica Bluetooth "Advance ADV-7011N 58mm" dibujando el ticket en la consola.
 */
public class ImpresoraTermicaConsola implements Impresora {

    private boolean conectada;

    @Override
    public String getNombre() {
        return "Termica 58mm (BlueTooth Printer) - vista en consola";
    }

    @Override
    public boolean estaConectada() {
        return conectada;
    }

    @Override
    public void conectar() {
        conectada = true;
    }

    @Override
    public void desconectar() {
        conectada = false;
    }

    @Override
    public void imprimir(String contenido) throws CocheraException {
        if (!conectada) {
            throw new CocheraException("La impresora no esta conectada.");
        }
        String borde = "+" + Texto.repetir('-', FormatoTermico.ANCHO + 2) + "+";
        System.out.println();
        System.out.println(borde);
        for (String linea : contenido.split("\n")) {
            System.out.println("| " + Texto.derecha(linea, FormatoTermico.ANCHO) + " |");
        }
        System.out.println(borde);
        System.out.println(Texto.centrar("~ corte de papel ~", FormatoTermico.ANCHO + 4));
    }
}
