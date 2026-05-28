/**
 * Punto de entrada alternativo para ejecutar el Programa 1 PSP0.1.
 * Permite iniciar el analizador con el comando java App y delega el
 * procesamiento real a AnalizadorLOC.
 */
public class App {
    /**
     * Ejecuta el analizador LOC usando los argumentos de consola recibidos.
     *
     * @param argumentos rutas de archivos Java a analizar
     */
    public static void main(String[] argumentos) {
        AnalizadorLOC.main(argumentos);
    }
}
