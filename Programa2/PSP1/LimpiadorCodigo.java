/**
 * Archivo de soporte para limpieza de comentarios del Programa 1 PSP0.1.
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Elimina comentarios de linea, bloque y JavaDoc sin alterar literales.
 * Conserva una linea de salida por cada linea fisica de entrada.
 */
public class LimpiadorCodigo {
    private static final char COMILLA_DOBLE = '"';
    private static final char COMILLA_SIMPLE = '\'';
    private static final char BARRA = '/';
    private static final char ASTERISCO = '*';
    private static final char ESCAPE = '\\';

    /**
     * Limpia comentarios del codigo fuente manteniendo el numero de lineas.
     *
     * @param lineasOriginales lineas fisicas originales
     * @return lineas sin comentarios, alineadas por numero de linea
     */
    public List<String> limpiarComentarios(List<String> lineasOriginales) {
        List<String> lineasLimpias = new ArrayList<String>();
        boolean dentroComentarioBloque = false;

        for (String linea : lineasOriginales) {
            StringBuilder lineaLimpia = new StringBuilder();
            boolean dentroCadena = false;
            boolean dentroCaracter = false;
            boolean escapeActivo = false;
            int indice = 0;

            while (indice < linea.length()) {
                char actual = linea.charAt(indice);
                char siguiente = obtenerSiguiente(linea, indice);

                if (dentroComentarioBloque) {
                    if (actual == ASTERISCO && siguiente == BARRA) {
                        dentroComentarioBloque = false;
                        indice += 2;
                    } else {
                        indice++;
                    }
                    continue;
                }

                if (!dentroCadena && !dentroCaracter && actual == BARRA
                        && siguiente == BARRA) {
                    break;
                }

                if (!dentroCadena && !dentroCaracter && actual == BARRA
                        && siguiente == ASTERISCO) {
                    dentroComentarioBloque = true;
                    indice += 2;
                    continue;
                }

                lineaLimpia.append(actual);

                if (escapeActivo) {
                    escapeActivo = false;
                } else if ((dentroCadena || dentroCaracter) && actual == ESCAPE) {
                    escapeActivo = true;
                } else if (!dentroCaracter && actual == COMILLA_DOBLE) {
                    dentroCadena = !dentroCadena;
                } else if (!dentroCadena && actual == COMILLA_SIMPLE) {
                    dentroCaracter = !dentroCaracter;
                }

                indice++;
            }

            lineasLimpias.add(lineaLimpia.toString());
        }

        return lineasLimpias;
    }

    private char obtenerSiguiente(String linea, int indice) {
        int siguienteIndice = indice + 1;
        if (siguienteIndice >= linea.length()) {
            return '\0';
        }
        return linea.charAt(siguienteIndice);
    }
}
