/**
 * Archivo de soporte para detectar clases del Programa 1 PSP0.1.
*/
package Clases;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detecta clases Java, sus rangos de lineas y sus metodos asociados.
 * Usa balanceo de llaves para soportar clases internas.
 */
public class AnalizadorClases {
    private static final Pattern PATRON_CLASE = Pattern.compile(
            "\\b(?:public\\s+|private\\s+|protected\\s+|abstract\\s+|final\\s+|"
            + "static\\s+)*class\\s+([A-Za-z_$][\\w$]*)\\b");
    private final AnalizadorMetodos analizadorMetodos;
    private final ContadorLOC contadorLOC;

    /**
     * Crea un analizador de clases con sus analizadores auxiliares.
     */
    public AnalizadorClases() {
        analizadorMetodos = new AnalizadorMetodos();
        contadorLOC = new ContadorLOC();
    }

    /**
     * Analiza clases y metodos dentro de las lineas limpias.
     *
     * @param lineasLimpias lineas sin comentarios
     * @return resultados de clases detectadas
     */
    public List<ResultadoClase> analizarClases(List<String> lineasLimpias) {
        List<ResultadoClase> resultados = new ArrayList<ResultadoClase>();
        Deque<ClaseEnProceso> pilaClases = new ArrayDeque<ClaseEnProceso>();
        int profundidadLlaves = 0;

        for (int indice = 0; indice < lineasLimpias.size(); indice++) {
            String linea = lineasLimpias.get(indice);
            int numeroLinea = indice + 1;
            cerrarClasesAntesDeLinea(pilaClases, profundidadLlaves, numeroLinea);

            Matcher matcherClase = PATRON_CLASE.matcher(linea);
            if (matcherClase.find()) {
                ResultadoClase resultadoClase = new ResultadoClase(matcherClase.group(1),
                        numeroLinea);
                resultados.add(resultadoClase);
                int nivelCierre = profundidadLlaves + contarCaracter(linea, '{')
                        - contarCaracter(linea, '}');
                pilaClases.push(new ClaseEnProceso(resultadoClase,
                        Math.max(nivelCierre, profundidadLlaves + 1)));
            } else if (!pilaClases.isEmpty()) {
                ResultadoClase claseActual = pilaClases.peek().resultadoClase;
                if (analizadorMetodos.esDeclaracionMetodo(linea,
                        claseActual.obtenerNombreClase())) {
                    claseActual.incrementarNumeroMetodos();
                }
            }

            profundidadLlaves += contarCaracter(linea, '{');
            profundidadLlaves -= contarCaracter(linea, '}');
            cerrarClasesAntesDeLinea(pilaClases, profundidadLlaves, numeroLinea + 1);
        }

        while (!pilaClases.isEmpty()) {
            ClaseEnProceso clase = pilaClases.pop();
            clase.resultadoClase.asignarLineaFin(lineasLimpias.size());
        }

        asignarTamanos(resultados, lineasLimpias);
        return resultados;
    }

    private void cerrarClasesAntesDeLinea(Deque<ClaseEnProceso> pilaClases,
            int profundidadLlaves, int siguienteLinea) {
        while (!pilaClases.isEmpty()
                && profundidadLlaves < pilaClases.peek().profundidadCierre) {
            ClaseEnProceso clase = pilaClases.pop();
            clase.resultadoClase.asignarLineaFin(Math.max(
                    clase.resultadoClase.obtenerLineaInicio(), siguienteLinea - 1));
        }
    }

    private void asignarTamanos(List<ResultadoClase> resultados,
            List<String> lineasLimpias) {
        for (ResultadoClase resultado : resultados) {
            int tamano = contadorLOC.contarLocEnRango(lineasLimpias,
                    resultado.obtenerLineaInicio(), resultado.obtenerLineaFin());
            resultado.asignarTamanoLoc(tamano);
        }
    }

    private int contarCaracter(String linea, char caracterObjetivo) {
        int contador = 0;
        boolean dentroCadena = false;
        boolean dentroCaracter = false;
        boolean escapeActivo = false;
        for (int indice = 0; indice < linea.length(); indice++) {
            char caracter = linea.charAt(indice);
            if (escapeActivo) {
                escapeActivo = false;
            } else if ((dentroCadena || dentroCaracter) && caracter == '\\') {
                escapeActivo = true;
            } else if (!dentroCaracter && caracter == '"') {
                dentroCadena = !dentroCadena;
            } else if (!dentroCadena && caracter == '\'') {
                dentroCaracter = !dentroCaracter;
            } else if (!dentroCadena && !dentroCaracter && caracter == caracterObjetivo) {
                contador++;
            }
        }
        return contador;
    }

    private static class ClaseEnProceso {
        private final ResultadoClase resultadoClase;
        private final int profundidadCierre;

        private ClaseEnProceso(ResultadoClase resultadoClase, int profundidadCierre) {
            this.resultadoClase = resultadoClase;
            this.profundidadCierre = profundidadCierre;
        }
    }
}
