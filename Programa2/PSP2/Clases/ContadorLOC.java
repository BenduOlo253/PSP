/**
 * Archivo de soporte para conteo LOC del Programa 1 PSP0.1.
 */
package Clases;
import java.util.List;

/**
 * Aplica el estandar de conteo PSP0.1 para lineas logicas Java.
 * Una linea fisica funcional cuenta como LOC y las declaraciones multiples
 * cuentan una LOC por variable declarada.
 */
public class ContadorLOC {
    private static final String SOLO_LLAVES_Y_PUNTOS = "[{};\\s]+";
    private final AnalizadorVariables analizadorVariables;

    /**
     * Crea un contador de LOC con un analizador de variables auxiliar.
     */
    public ContadorLOC() {
        analizadorVariables = new AnalizadorVariables();
    }

    /**
     * Cuenta las LOC logicas totales de un conjunto de lineas limpias.
     *
     * @param lineasLimpias lineas sin comentarios
     * @return total de LOC logicas
     */
    public int contarLocTotal(List<String> lineasLimpias) {
        int total = 0;
        for (String linea : lineasLimpias) {
            total += contarLocLinea(linea);
        }
        return total;
    }

    /**
     * Cuenta las LOC dentro de un intervalo inclusivo de lineas.
     *
     * @param lineasLimpias lineas sin comentarios
     * @param lineaInicio linea inicial en base uno
     * @param lineaFin linea final en base uno
     * @return LOC del intervalo
     */
    public int contarLocEnRango(List<String> lineasLimpias, int lineaInicio,
            int lineaFin) {
        int total = 0;
        int inicio = Math.max(lineaInicio, 1);
        int fin = Math.min(lineaFin, lineasLimpias.size());
        for (int indice = inicio - 1; indice < fin; indice++) {
            total += contarLocLinea(lineasLimpias.get(indice));
        }
        return total;
    }

    /**
     * Cuenta la contribucion LOC de una linea fisica limpia.
     *
     * @param linea linea sin comentarios
     * @return LOC de la linea
     */
    public int contarLocLinea(String linea) {
        String normalizada = linea.trim();
        if (!esLineaFuncional(normalizada)) {
            return 0;
        }
        if (analizadorVariables.esDeclaracionVariable(normalizada)) {
            return analizadorVariables.contarDeclaracionesEnLinea(normalizada);
        }
        return 1;
    }

    /**
     * Determina si una linea contiene codigo funcional contabilizable.
     *
     * @param linea linea sin comentarios y potencialmente recortada
     * @return true si debe contarse como LOC
     */
    public boolean esLineaFuncional(String linea) {
        String normalizada = linea.trim();
        if (normalizada.isEmpty()) {
            return false;
        }
        String sinLlaves = normalizada.replace("}", "").trim();
        if (sinLlaves.isEmpty() || normalizada.matches(SOLO_LLAVES_Y_PUNTOS)) {
            return false;
        }
        if (sinLlaves.equals(";") || sinLlaves.equals("{") || sinLlaves.equals("};")) {
            return false;
        }
        return true;
    }
}
