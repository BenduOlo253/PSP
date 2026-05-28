/**
 * Interfaz de terminal para que el usuario seleccione archivos Java y ejecute
 * el analizador LOC PSP0.1 sin recordar argumentos de consola.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Presenta un menu interactivo y regresa siempre al inicio despues de errores
 * amigables, evitando trazas tecnicas para el usuario final.
 */
public class InterfazTerminal {
    private static final String OPCION_ANALIZAR = "1";
    private static final String OPCION_AYUDA = "2";
    private static final String OPCION_SALIR = "3";
    private static final String SEPARADOR_RUTAS = ",";
    private final AnalizadorLOC analizadorLOC;

    /**
     * Crea una interfaz de terminal con un analizador LOC reutilizable.
     */
    public InterfazTerminal() {
        analizadorLOC = new AnalizadorLOC();
    }

    /**
     * Inicia el ciclo de menu interactivo.
     */
    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            imprimirMenu();
            try {
                String opcion = scanner.nextLine().trim();
                if (OPCION_ANALIZAR.equals(opcion)) {
                    solicitarArchivos(scanner);
                } else if (OPCION_AYUDA.equals(opcion)) {
                    imprimirAyuda();
                } else if (OPCION_SALIR.equals(opcion)) {
                    continuar = false;
                    System.out.println("Gracias por usar el Analizador LOC PSP0.1.");
                } else {
                    System.out.println("Opcion no valida. Intente nuevamente.");
                }
            } catch (NoSuchElementException excepcion) {
                System.out.println("Entrada finalizada. Cerrando el programa de forma segura.");
                continuar = false;
            } catch (RuntimeException excepcion) {
                System.out.println("No fue posible completar la operacion: "
                        + excepcion.getMessage());
                System.out.println("Regresando al menu principal. Puede intentarlo nuevamente.");
            }
        }
    }

    private void imprimirMenu() {
        System.out.println();
        System.out.println("=== Analizador LOC PSP0.1 ===");
        System.out.println("1. Analizar archivo(s) Java");
        System.out.println("2. Ver instrucciones");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private void solicitarArchivos(Scanner scanner) {
        System.out.println();
        System.out.println("Ingrese una o varias rutas .java separadas por coma.");
        System.out.println("Ejemplo: ProgramaPrueba1.java, ProgramaPrueba2.java");
        System.out.print("Ruta(s): ");
        String entrada = scanner.nextLine().trim();
        String[] rutas = obtenerRutas(entrada);
        if (rutas.length == 0) {
            System.out.println("No ingreso rutas validas. Regresando al menu principal.");
            return;
        }
        analizadorLOC.procesarArchivos(rutas);
        System.out.println("Analisis finalizado. Regresando al menu principal.");
    }

    private String[] obtenerRutas(String entrada) {
        String[] partes = entrada.split(SEPARADOR_RUTAS);
        List<String> rutas = new ArrayList<String>();
        for (String parte : partes) {
            String ruta = parte.trim();
            if (!ruta.isEmpty()) {
                rutas.add(ruta);
            }
        }
        return rutas.toArray(new String[rutas.size()]);
    }

    private void imprimirAyuda() {
        System.out.println();
        System.out.println("Instrucciones:");
        System.out.println("- Compile desde Programa2/PSP1 con: javac *.java");
        System.out.println("- Ejecute el menu con: java App");
        System.out.println("- Tambien puede usar argumentos: java App Archivo.java");
        System.out.println("- En el menu, separe multiples rutas con coma.");
        System.out.println("- Si ocurre un error, el programa mostrara un mensaje amigable");
        System.out.println("  y regresara al menu principal.");
    }
}
