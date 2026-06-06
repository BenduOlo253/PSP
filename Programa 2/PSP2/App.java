import Clases.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Nombre del archivo: App.java
 * Autor: Jose Manuel Roberto Badillo
 * Fecha de creación: 31/05/2026
 * Versión: 1.0
 * Descripción: Clase principal para ejecutar el contador LOC.
 */
public class App {

    private static final String OPCION_SALIR = "0";

    /**
     * Punto de entrada del programa.
     * Permite al usuario ingresar rutas de archivos Java para analizar su LOC.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProcesadorArchivo procesadorArchivo = new ProcesadorArchivo();
        LimpiadorCodigo limpiadorCodigo = new LimpiadorCodigo();
        ContadorLOC contadorLOC = new ContadorLOC();

        int numeroPrograma = 1;
        boolean continuar = true;

        while (continuar) {
            try {
                mostrarMenu();

                System.out.print("Ingresa la ruta del archivo Java: ");
                String rutaArchivo = scanner.nextLine().trim();

                if (rutaArchivo.equals(OPCION_SALIR)) {
                    continuar = false;
                    System.out.println("Programa finalizado correctamente.");
                    continue;
                }

                // Se valida antes de leer para evitar errores por rutas inválidas.
                procesadorArchivo.validarArchivoJava(rutaArchivo);

                List<String> lineasOriginales = procesadorArchivo.leerArchivo(rutaArchivo);

                // El limpiador conserva las líneas, pero elimina contenido no contable.
                List<String> lineasLimpias = limpiadorCodigo.limpiarCodigo(lineasOriginales);

                if (!procesadorArchivo.contieneCodigo(lineasLimpias)) {
                    System.out.println("El archivo no contiene código Java contable.");
                    continue;
                }

                ContadorLOC.ResultadoAnalisis resultado = contadorLOC.analizarCodigo(
                        lineasLimpias,
                        lineasOriginales.size(),
                        numeroPrograma,
                        rutaArchivo
                );

                mostrarResultado(resultado);
                numeroPrograma++;

            } catch (IOException excepcion) {
                System.out.println("No se pudo leer el archivo.");
                System.out.println("Detalle: " + excepcion.getMessage());
            } catch (IllegalArgumentException excepcion) {
                System.out.println("Entrada inválida.");
                System.out.println("Detalle: " + excepcion.getMessage());
            } catch (Exception excepcion) {
                // Este catch evita que la aplicación termine por errores no previstos.
                System.out.println("Ocurrió un error inesperado.");
                System.out.println("Detalle: " + excepcion.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("======================================");
        System.out.println("        CONTADOR LOC");
        System.out.println("======================================");
        System.out.println("Ingresa la ruta de un archivo .java");
        System.out.println("Escribe 0 para salir");
        System.out.println();
    }

    private static void mostrarResultado(ContadorLOC.ResultadoAnalisis resultado) {
        System.out.println();
        System.out.println("Resultado del análisis");
        System.out.println("Archivo analizado: " + resultado.getNombreArchivo());
        System.out.println("------------------------------------------------------------");

        System.out.printf(
                "%-12s %-20s %-18s %-18s %-14s%n",
                "Programa",
                "Clase",
                "Metodos",
                "Tam. clase",
                "Tam. total"
        );

        if (resultado.getResultadosClase().isEmpty()) {
            System.out.printf(
                    "%-12d %-20s %-18d %-18d %-14d%n",
                    resultado.getNumeroPrograma(),
                    "Sin clases",
                    0,
                    0,
                    resultado.getTotalLOC()
            );
        } else {
            for (int indice = 0; indice < resultado.getResultadosClase().size();
                    indice++) {
                ContadorLOC.ResultadoClase clase = resultado
                        .getResultadosClase()
                        .get(indice);

                String total = "";

                // El tamaño total se imprime solo en la última fila del programa.
                if (indice == resultado.getResultadosClase().size() - 1) {
                    total = String.valueOf(resultado.getTotalLOC());
                }

                System.out.printf(
                        "%-12d %-20s %-18d %-18d %-14s%n",
                        resultado.getNumeroPrograma(),
                        clase.getNombreClase(),
                        clase.getNumeroMetodos(),
                        clase.getTamanioClase(),
                        total
                );
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Total de líneas físicas del archivo: "
                + resultado.getTotalLineasFisicas());
        System.out.println("Total de LOC lógicas contadas: "
                + resultado.getTotalLOC());
        System.out.println("Total de clases: "
                + resultado.getResultadosClase().size());

        if (resultado.getVariablesInicializadas().isEmpty()) {
            System.out.println("Variables declaradas e inicializadas: ninguna detectada.");
        } else {
            System.out.println("Variables declaradas e inicializadas:");

            for (String variable : resultado.getVariablesInicializadas()) {
                System.out.println("Variable " + variable
                        + " declarada e inicializada en la misma línea");
            }
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Ingrese enter para continuar...");
        Scanner scanner = new Scanner(System.in);
        String salida = scanner.nextLine();
    }
}