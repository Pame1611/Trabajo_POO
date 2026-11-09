package cochera.impresion;

import cochera.excepcion.CocheraException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * "Imprime" cada ticket como un archivo .txt dentro de la carpeta impresiones/.
 * Sirve para guardar evidencia de lo impreso durante la demostración.
 */
public class ImpresoraArchivo implements Impresora {

    private static final DateTimeFormatter NOMBRE = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final File carpeta;
    private boolean conectada;
    private int contador;

    public ImpresoraArchivo(File carpeta) {
        this.carpeta = carpeta;
    }

    @Override
    public String getNombre() {
        return "Archivos .txt en " + carpeta.getPath() + File.separator;
    }

    @Override
    public boolean estaConectada() {
        return conectada;
    }

    @Override
    public void conectar() throws CocheraException {
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new CocheraException("No se pudo crear la carpeta " + carpeta.getPath());
        }
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
        contador++;
        File archivo = new File(carpeta, "ticket_" + LocalDateTime.now().format(NOMBRE) + "_" + contador + ".txt");
        try {
            Files.write(archivo.toPath(), contenido.getBytes(StandardCharsets.UTF_8));
            System.out.println("[IMPRESORA] Ticket guardado en " + archivo.getPath());
        } catch (IOException e) {
            throw new CocheraException("Error al imprimir: " + e.getMessage(), e);
        }
    }
}
