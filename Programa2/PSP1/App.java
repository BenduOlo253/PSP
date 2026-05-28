/**
 * Punto de entrada principal para ejecutar el Programa 1 PSP0.1.
 * Permite usar argumentos directos o una interfaz de terminal interactiva.
 */
public class App {
    /**
     * Ejecuta el programa con argumentos directos o inicia el menu interactivo.
     *
     * @param argumentos rutas de archivos Java a analizar; si no se reciben,
     *        se muestra la interfaz de terminal
     */
    public static void main(String[] argumentos) {
        if (argumentos.length > 0) {
            new AnalizadorLOC().procesarArchivos(argumentos);
            return;
        }
        new InterfazTerminal().iniciar();
    }
}
