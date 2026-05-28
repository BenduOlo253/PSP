/**
 * Archivo de soporte para lectura de fuentes del Programa 1 PSP0.1.
 */
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Lee archivos fuente Java y conserva sus lineas fisicas originales.
 * Forma parte del Programa 1 de PSP0.1 para conteo de LOC.
 */
public class ProcesadorArchivo {
    /**
     * Verifica que la ruta corresponda a un archivo regular y legible.
     *
     * @param rutaArchivo ruta del archivo fuente Java
     * @throws IOException si el archivo no existe o no puede leerse
     */
    public void validarArchivoLegible(Path rutaArchivo) throws IOException {
        if (!Files.exists(rutaArchivo)) {
            throw new IOException("El archivo no existe.");
        }
        if (!Files.isRegularFile(rutaArchivo)) {
            throw new IOException("La ruta no corresponde a un archivo regular.");
        }
        if (!Files.isReadable(rutaArchivo)) {
            throw new IOException("El archivo no tiene permisos de lectura.");
        }
    }

    /**
     * Lee todas las lineas de un archivo usando UTF-8.
     *
     * @param rutaArchivo ruta del archivo fuente Java
     * @return lineas fisicas del archivo
     * @throws IOException si el archivo no puede leerse
     */
    public List<String> leerLineas(Path rutaArchivo) throws IOException {
        return Files.readAllLines(rutaArchivo, StandardCharsets.UTF_8);
    }

    /**
     * Cuenta las lineas fisicas entregadas.
     *
     * @param lineas lineas fisicas del archivo
     * @return numero total de lineas fisicas
     */
    public int contarLineasFisicas(List<String> lineas) {
        return lineas.size();
    }
}
