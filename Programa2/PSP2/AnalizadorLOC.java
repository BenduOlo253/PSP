/**
 * Archivo principal del Programa 1 PSP0.1 para analisis LOC.
 */
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Programa principal de consola para calcular metricas PSP0.1 de LOC en Java.
 * Procesa uno o varios archivos fuente y reporta clases, metodos y variables.
 */
public class AnalizadorLOC {
    private static final int ANCHO_PROGRAMA = 18;
    private static final int ANCHO_CLASE = 22;
    private static final int ANCHO_METODOS = 19;
    private static final int ANCHO_TAMANO_CLASE = 20;
    private static final String FORMATO_FILA = "%-18s | %-22s | %-19s | %-20s | %-12s%n";

    /**
     * Punto de entrada del programa.
     *
     * @param argumentos rutas de archivos Java a procesar
     */
    public static void main(String[] argumentos) {
        if (argumentos.length == 0) {
            imprimirUso();
            return;
        }

        ProcesadorArchivo procesadorArchivo = new ProcesadorArchivo();
        LimpiadorCodigo limpiadorCodigo = new LimpiadorCodigo();
        ContadorLOC contadorLOC = new ContadorLOC();
        AnalizadorClases analizadorClases = new AnalizadorClases();
        AnalizadorVariables analizadorVariables = new AnalizadorVariables();

        for (int indice = 0; indice < argumentos.length; indice++) {
            Path rutaArchivo = Paths.get(argumentos[indice]);
            try {
                List<String> lineasOriginales = procesadorArchivo.leerLineas(rutaArchivo);
                List<String> lineasLimpias = limpiadorCodigo.limpiarComentarios(lineasOriginales);
                int totalLineasFisicas = procesadorArchivo.contarLineasFisicas(lineasOriginales);
                int locTotal = contadorLOC.contarLocTotal(lineasLimpias);
                List<ResultadoClase> resultadosClase = analizadorClases.analizarClases(lineasLimpias);
                List<String> variables = analizadorVariables
                        .detectarVariablesInicializadas(lineasLimpias);
                ResultadoPrograma resultado = new ResultadoPrograma(indice + 1,
                        rutaArchivo.getFileName().toString(), totalLineasFisicas,
                        locTotal, resultadosClase, variables);
                imprimirResultado(resultado);
            } catch (IOException excepcion) {
                System.err.println("No fue posible leer el archivo " + rutaArchivo
                        + ": " + excepcion.getMessage());
            }
        }
    }

    private static void imprimirUso() {
        System.out.println("Uso: java AnalizadorLOC Archivo1.java [Archivo2.java ...]");
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
                + ANCHO_TAMANO_CLASE + 24;
        StringBuilder separador = new StringBuilder();
        for (int indice = 0; indice < longitud; indice++) {
            separador.append('-');
        }
        System.out.println(separador.toString());
    }
}
