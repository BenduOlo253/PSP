/**
 * Archivo principal del Programa 1 PSP0.1 para analisis LOC.
 */
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Orquestador para calcular metricas PSP0.1 de LOC en archivos Java.
 * Procesa uno o varios archivos fuente y reporta clases, metodos y variables.
 */
public class AnalizadorLOC {
    private static final int ANCHO_PROGRAMA = 18;
    private static final int ANCHO_CLASE = 22;
    private static final int ANCHO_METODOS = 19;
    private static final int ANCHO_TAMANO_CLASE = 20;
    private static final int AJUSTE_SEPARADOR = 24;
    private static final String EXTENSION_JAVA = ".java";
    private static final String FORMATO_FILA = "%-18s | %-22s | %-19s | %-20s | %-12s%n";
    private final ProcesadorArchivo procesadorArchivo;
    private final LimpiadorCodigo limpiadorCodigo;
    private final ContadorLOC contadorLOC;
    private final AnalizadorClases analizadorClases;
    private final AnalizadorVariables analizadorVariables;

    /**
     * Crea un analizador con todas sus dependencias de procesamiento.
     */
    public AnalizadorLOC() {
        procesadorArchivo = new ProcesadorArchivo();
        limpiadorCodigo = new LimpiadorCodigo();
        contadorLOC = new ContadorLOC();
        analizadorClases = new AnalizadorClases();
        analizadorVariables = new AnalizadorVariables();
    }

    /**
     * Punto de entrada alternativo para ejecutar el analizador con argumentos.
     *
     * @param argumentos rutas de archivos Java a procesar
     */
    public static void main(String[] argumentos) {
        if (argumentos.length == 0) {
            imprimirUso();
            return;
        }
        new AnalizadorLOC().procesarArchivos(argumentos);
    }

    /**
     * Procesa una lista de rutas Java e imprime resultados por archivo.
     * Cualquier error se reporta de forma amigable y no detiene el menu.
     *
     * @param rutasArchivos rutas de archivos Java a procesar
     */
    public void procesarArchivos(String[] rutasArchivos) {
        if (rutasArchivos == null || rutasArchivos.length == 0) {
            imprimirUso();
            return;
        }

        for (int indice = 0; indice < rutasArchivos.length; indice++) {
            procesarArchivo(rutasArchivos[indice], indice + 1);
        }
    }

    private void procesarArchivo(String rutaArchivoTexto, int numeroPrograma) {
        try {
            Path rutaArchivo = Paths.get(rutaArchivoTexto.trim());
            validarExtensionJava(rutaArchivo);
            ResultadoPrograma resultado = analizarArchivo(rutaArchivo, numeroPrograma);
            imprimirResultado(resultado);
        } catch (InvalidPathException excepcion) {
            imprimirErrorAmigable(rutaArchivoTexto,
                    "La ruta indicada no tiene un formato valido.");
        } catch (IOException excepcion) {
            imprimirErrorAmigable(rutaArchivoTexto, excepcion.getMessage());
        } catch (RuntimeException excepcion) {
            imprimirErrorAmigable(rutaArchivoTexto,
                    "Ocurrio un problema durante el analisis. " + excepcion.getMessage());
        }
    }

    private ResultadoPrograma analizarArchivo(Path rutaArchivo, int numeroPrograma)
            throws IOException {
        procesadorArchivo.validarArchivoLegible(rutaArchivo);
        List<String> lineasOriginales = procesadorArchivo.leerLineas(rutaArchivo);
        List<String> lineasLimpias = limpiadorCodigo.limpiarComentarios(lineasOriginales);
        int totalLineasFisicas = procesadorArchivo.contarLineasFisicas(lineasOriginales);
        int locTotal = contadorLOC.contarLocTotal(lineasLimpias);
        List<ResultadoClase> resultadosClase = analizadorClases.analizarClases(lineasLimpias);
        List<String> variables = analizadorVariables.detectarVariablesInicializadas(lineasLimpias);

        return new ResultadoPrograma(numeroPrograma, rutaArchivo.getFileName().toString(),
                totalLineasFisicas, locTotal, resultadosClase, variables);
    }

    private void validarExtensionJava(Path rutaArchivo) throws IOException {
        String nombreArchivo = rutaArchivo.getFileName().toString().toLowerCase();
        if (!nombreArchivo.endsWith(EXTENSION_JAVA)) {
            throw new IOException("El archivo debe tener extension .java.");
        }
    }

    private static void imprimirUso() {
        System.out.println("Uso directo: java AnalizadorLOC Archivo1.java [Archivo2.java ...]");
        System.out.println("Uso recomendado con menu: java App");
    }

    private static void imprimirErrorAmigable(String rutaArchivo, String mensaje) {
        System.out.println();
        System.out.println("No fue posible analizar el archivo: " + rutaArchivo);
        System.out.println("Motivo: " + mensaje);
        System.out.println("Revise la ruta e intente nuevamente desde el menu principal.");
    }

    private static void imprimirResultado(ResultadoPrograma resultado) {
        System.out.println();
        System.out.println("Programa " + resultado.obtenerNumeroPrograma() + ": "
                + resultado.obtenerNombreArchivo());
        imprimirSeparador();
        System.out.printf(FORMATO_FILA, "Número de Programa", "Nombre de la clase",
                "Número de métodos", "Tamaño de la clase", "Tamaño total");
        imprimirSeparador();

        List<ResultadoClase> clases = resultado.obtenerResultadosClase();
        for (int indice = 0; indice < clases.size(); indice++) {
            ResultadoClase clase = clases.get(indice);
            String tamanoTotal = indice == clases.size() - 1
                    ? String.valueOf(resultado.obtenerLocTotal()) : "";
            System.out.printf(FORMATO_FILA, resultado.obtenerNumeroPrograma(),
                    clase.obtenerNombreClase(), clase.obtenerNumeroMetodos(),
                    clase.obtenerTamanoLoc(), tamanoTotal);
        }
        if (clases.isEmpty()) {
            System.out.printf(FORMATO_FILA, resultado.obtenerNumeroPrograma(),
                    "Sin clases", 0, 0, resultado.obtenerLocTotal());
        }
        imprimirSeparador();

        System.out.println("Total de líneas físicas del archivo: "
                + resultado.obtenerTotalLineasFisicas());
        System.out.println("Total de LOC lógicas contadas: " + resultado.obtenerLocTotal());
        System.out.println("Total de clases: " + resultado.obtenerTotalClases());

        System.out.println("Variables declaradas e inicializadas:");
        if (resultado.obtenerVariablesInicializadas().isEmpty()) {
            System.out.println("No se detectaron variables declaradas e inicializadas.");
        } else {
            for (String variable : resultado.obtenerVariablesInicializadas()) {
                System.out.println(variable);
            }
        }
    }

    private static void imprimirSeparador() {
        int longitud = ANCHO_PROGRAMA + ANCHO_CLASE + ANCHO_METODOS
                + ANCHO_TAMANO_CLASE + AJUSTE_SEPARADOR;
        StringBuilder separador = new StringBuilder();
        for (int indice = 0; indice < longitud; indice++) {
            separador.append('-');
        }
        System.out.println(separador.toString());
    }
}
