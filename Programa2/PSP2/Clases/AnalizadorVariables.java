/**
 * Archivo de soporte para detectar variables del Programa 1 PSP0.1.
 */
package Clases;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detecta variables declaradas e inicializadas en la misma linea fisica.
 * Evita asignaciones simples, incrementos y comparaciones.
 */
public class AnalizadorVariables {
    private static final String MODIFICADORES = "(?:public|private|protected|static|final|"
            + "transient|volatile)\\s+";
    private static final String TIPO = "[A-Za-z_$][\\w$]*(?:\\s*<[^;=(){}]+>)?"
            + "(?:\\s*\\[\\s*\\])*";
    private static final Pattern PATRON_DECLARACION = Pattern.compile(
            "^\\s*(?:(?:" + MODIFICADORES + ")*)" + TIPO
            + "\\s+(.+;)\\s*$");
    private static final Pattern PATRON_NOMBRE = Pattern.compile(
            "^\\s*([A-Za-z_$][\\w$]*)\\s*(?:\\[\\s*\\])?\\s*=.*");

    /**
     * Analiza todas las lineas y devuelve mensajes por variable detectada.
     *
     * @param lineasLimpias lineas sin comentarios
     * @return mensajes de variables declaradas e inicializadas
     */
    public List<String> detectarVariablesInicializadas(List<String> lineasLimpias) {
        List<String> hallazgos = new ArrayList<String>();
        for (String linea : lineasLimpias) {
            hallazgos.addAll(detectarEnLinea(linea));
        }
        return hallazgos;
    }

    /**
     * Detecta variables inicializadas en una linea especifica.
     *
     * @param linea linea sin comentarios
     * @return mensajes de variables encontradas
     */
    public List<String> detectarEnLinea(String linea) {
        List<String> hallazgos = new ArrayList<String>();
        String normalizada = linea.trim();

        if (normalizada.isEmpty() || esEstructuraNoDeclarativa(normalizada)) {
            return hallazgos;
        }

        Matcher declaracion = PATRON_DECLARACION.matcher(normalizada);
        if (!declaracion.matches()) {
            return hallazgos;
        }

        String segmentoVariables = quitarPuntoYComaFinal(declaracion.group(1));
        List<String> declaradores = dividirDeclaradores(segmentoVariables);
        for (String declarador : declaradores) {
            Matcher nombre = PATRON_NOMBRE.matcher(declarador.trim());
            if (nombre.matches()) {
                hallazgos.add("Variable " + nombre.group(1)
                        + " declarada e inicializada en la misma línea");
            }
        }
        return hallazgos;
    }

    /**
     * Determina si una linea parece una declaracion de variables.
     *
     * @param linea linea sin comentarios
     * @return true si parece declaracion de variables
     */
    public boolean esDeclaracionVariable(String linea) {
        String normalizada = linea.trim();
        if (normalizada.isEmpty() || esEstructuraNoDeclarativa(normalizada)
                || !normalizada.endsWith(";")) {
            return false;
        }
        return PATRON_DECLARACION.matcher(normalizada).matches();
    }

    /**
     * Cuenta las variables declaradas en una linea de declaracion.
     *
     * @param linea linea sin comentarios
     * @return numero de declaradores de variable
     */
    public int contarDeclaracionesEnLinea(String linea) {
        Matcher declaracion = PATRON_DECLARACION.matcher(linea.trim());
        if (!declaracion.matches()) {
            return 1;
        }
        String segmentoVariables = quitarPuntoYComaFinal(declaracion.group(1));
        return dividirDeclaradores(segmentoVariables).size();
    }

    private boolean esEstructuraNoDeclarativa(String linea) {
        return linea.startsWith("if ") || linea.startsWith("if(")
                || linea.startsWith("for ") || linea.startsWith("for(")
                || linea.startsWith("while ") || linea.startsWith("while(")
                || linea.startsWith("switch ") || linea.startsWith("switch(")
                || linea.startsWith("catch ") || linea.startsWith("catch(")
                || linea.startsWith("return ") || linea.startsWith("throw ")
                || linea.startsWith("class ") || linea.contains(" class ")
                || linea.startsWith("import ") || linea.startsWith("package ");
    }

    private String quitarPuntoYComaFinal(String texto) {
        String resultado = texto.trim();
        if (resultado.endsWith(";")) {
            resultado = resultado.substring(0, resultado.length() - 1);
        }
        return resultado;
    }

    private List<String> dividirDeclaradores(String texto) {
        List<String> partes = new ArrayList<String>();
        StringBuilder actual = new StringBuilder();
        int profundidad = 0;
        boolean dentroCadena = false;
        boolean dentroCaracter = false;
        boolean escapeActivo = false;

        for (int indice = 0; indice < texto.length(); indice++) {
            char caracter = texto.charAt(indice);
            if (escapeActivo) {
                escapeActivo = false;
            } else if ((dentroCadena || dentroCaracter) && caracter == '\\') {
                escapeActivo = true;
            } else if (!dentroCaracter && caracter == '"') {
                dentroCadena = !dentroCadena;
            } else if (!dentroCadena && caracter == '\'') {
                dentroCaracter = !dentroCaracter;
            } else if (!dentroCadena && !dentroCaracter) {
                if (caracter == '(' || caracter == '<' || caracter == '[' || caracter == '{') {
                    profundidad++;
                } else if (caracter == ')' || caracter == '>' || caracter == ']' || caracter == '}') {
                    profundidad--;
                } else if (caracter == ',' && profundidad == 0) {
                    partes.add(actual.toString());
                    actual.setLength(0);
                    continue;
                }
            }
            actual.append(caracter);
        }

        if (actual.length() > 0) {
            partes.add(actual.toString());
        }
        return partes;
    }
}
