/**
 * Nombre del archivo: ProcesadorArchivo.java
 * Autor: Jose Manuel Roberto Badillo
 * Fecha de creacion: 31/05/2026
 * Version: 1.0
 * Descripcion: Procesa y valida archivos fuente Java.
 */

package Clases;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProcesadorArchivo {

    private static final String EXTENSION_JAVA = ".java";

    /**
     * Valida que la ruta pertenezca a un archivo Java legible.
     *
     * @param rutaArchivo ruta del archivo
     */
    public void validarArchivoJava(String rutaArchivo) {
        validarRuta(rutaArchivo);

        if (!esArchivoJava(rutaArchivo)) {
            throw new IllegalArgumentException("El archivo debe tener extension .java.");
        }

        Path ruta = Paths.get(rutaArchivo);

        if (!Files.exists(ruta)) {
            throw new IllegalArgumentException("El archivo no existe.");
        }

        if (!Files.isRegularFile(ruta)) {
            throw new IllegalArgumentException("La ruta no pertenece a un archivo.");
        }

        if (!Files.isReadable(ruta)) {
            throw new IllegalArgumentException("El archivo no se puede leer.");
        }
    }

    /**
     * Lee todas las lineas fisicas del archivo.
     *
     * @param rutaArchivo ruta del archivo
     * @return lineas fisicas del archivo
     * @throws IOException si ocurre un error al leer
     */
    public List<String> leerArchivo(String rutaArchivo) throws IOException {
        validarArchivoJava(rutaArchivo);
        return Files.readAllLines(Paths.get(rutaArchivo));
    }

    /**
     * Verifica si el archivo tiene extension Java.
     *
     * @param rutaArchivo ruta del archivo
     * @return true si termina en .java
     */
    public boolean esArchivoJava(String rutaArchivo) {
        return rutaArchivo != null
                && rutaArchivo.trim().toLowerCase().endsWith(EXTENSION_JAVA);
    }

    /**
     * Determina si existen lineas de codigo contable.
     *
     * @param lineasLimpias lineas despues de limpiar comentarios
     * @return true si hay codigo contable
     */
    public boolean contieneCodigo(List<String> lineasLimpias) {
        if (lineasLimpias == null || lineasLimpias.isEmpty()) {
            return false;
        }

        for (String linea : lineasLimpias) {
            if (linea != null && esLineaConCodigo(linea.trim())) {
                return true;
            }
        }

        return false;
    }

    private void validarRuta(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta no puede estar vacia.");
        }
    }

    private boolean esLineaConCodigo(String linea) {
        // Las llaves solas no representan codigo funcional segun el estandar.
        return !linea.isEmpty()
                && !linea.equals("{")
                && !linea.equals("}")
                && !linea.equals(";")
                && !linea.equals("};");
    }
}