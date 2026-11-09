package cochera.impresion;

import cochera.excepcion.CocheraException;

/**
 * INTERFAZ de impresora. La web usaba Web Bluetooth con una térmica de 58 mm;
 * aquí hay dos implementaciones intercambiables (consola y archivo).
 */
public interface Impresora {

    String getNombre();

    boolean estaConectada();

    void conectar() throws CocheraException;

    void desconectar();

    /** Imprime un texto ya maquetado a 32 columnas. */
    void imprimir(String contenido) throws CocheraException;
}
