/**
 * Nombre del archivo: ContadorLOC.java
 * Autor: Jose Manuel Roberto Badillo
 * Fecha de creacion: 31/05/2026
 * Version: 1.0
 * Descripcion: Cuenta LOC, clases, metodos y variables inicializadas.
 */

package Clases;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContadorLOC {

    private static final Pattern PATRON_CLASE = Pattern.compile(
            ".*\\bclass\\s+([A-Za-z_$][\\w$]*).*"
    );

    private static final String[] PALABRAS_CONTROL = {
        "if", "for", "while", "switch", "catch", "try", "else", "do"
    };

    private static final String[] MODIFICADORES = {
        "public", "private", "protected", "static", "final", "abstract",
        "synchronized", "native", "strictfp", "default", "transient", "volatile"
    };

    /**
     * Analiza el codigo completo de un archivo Java.
     *
     * @param lineasCodigo lineas limpias del archivo
     * @param totalLineasFisicas total de lineas fisicas originales
     * @param numeroPrograma numero de programa analizado
     * @param nombreArchivo nombre o ruta del archivo
     * @return resultado del analisis
     */
    public ResultadoAnalisis analizarCodigo(List<String> lineasCodigo,
            int totalLineasFisicas, int numeroPrograma, String nombreArchivo) {
        ResultadoAnalisis resultado = new ResultadoAnalisis(
                numeroPrograma,
                nombreArchivo,
                totalLineasFisicas
        );

        resultado.setTotalLOC(contarLOC(lineasCodigo));
        analizarClases(lineasCodigo, resultado);
        analizarVariables(lineasCodigo, resultado);

        return resultado;
    }

    /**
     * Cuenta el total de LOC de un archivo limpio.
     *
     * @param lineasCodigo lineas limpias del archivo
     * @return total de LOC
     */
    public int contarLOC(List<String> lineasCodigo) {
        int totalLOC = 0;

        if (lineasCodigo == null) {
            return totalLOC;
        }

        List<String> lineasLogicas = normalizarLineasLogicas(lineasCodigo);

        for (String linea : lineasLogicas) {
            totalLOC += contarLinea(linea);
        }

        return totalLOC;
    }

    /**
     * Cuenta las LOC de una linea especifica.
     *
     * @param linea linea de codigo
     * @return cantidad de LOC de la linea
     */
    public int contarLinea(String linea) {
        if (linea == null) {
            return 0;
        }

        String lineaLimpia = linea.trim();

        if (lineaLimpia.isEmpty() || esLlaveSola(lineaLimpia)) {
            return 0;
        }

        // Las declaraciones multiples cuentan una LOC por variable.
        if (esDeclaracionMultiple(lineaLimpia)) {
            return contarDeclaracionesMultiples(lineaLimpia);
        }

        return 1;
    }

    /**
     * Agrupa lineas fisicas que pertenecen a una misma instruccion Java.
     * Esto permite que una instruccion partida por formato visual cuente como
     * una sola LOC logica, por ejemplo un println dividido en varias lineas.
     *
     * @param lineasCodigo lineas limpias del archivo
     * @return instrucciones logicas normalizadas
     */
    private List<String> normalizarLineasLogicas(List<String> lineasCodigo) {
        List<String> lineasLogicas = new ArrayList<>();

        if (lineasCodigo == null) {
            return lineasLogicas;
        }

        StringBuilder acumulador = new StringBuilder();

        for (String linea : lineasCodigo) {
            if (linea == null) {
                continue;
            }

            String lineaLimpia = linea.trim();

            if (lineaLimpia.isEmpty()) {
                continue;
            }

            if (acumulador.length() > 0) {
                acumulador.append(' ');
            }

            acumulador.append(lineaLimpia);

            if (terminaInstruccionLogica(lineaLimpia)) {
                lineasLogicas.add(acumulador.toString().trim());
                acumulador.setLength(0);
            }
        }

        if (acumulador.length() > 0) {
            lineasLogicas.add(acumulador.toString().trim());
        }

        return lineasLogicas;
    }

    /**
     * Determina si la linea actual cierra una instruccion logica.
     * Se consideran cierres validos el punto y coma, la apertura de bloque
     * y el cierre de bloque.
     *
     * @param linea linea limpia actual
     * @return true si la instruccion logica termino
     */
    private boolean terminaInstruccionLogica(String linea) {
        if (linea == null) {
            return false;
        }

        String lineaLimpia = linea.trim();

        if (lineaLimpia.isEmpty()) {
            return false;
        }

        return lineaLimpia.endsWith(";")
                || lineaLimpia.endsWith("{")
                || lineaLimpia.endsWith("}");
    }


    private void analizarClases(List<String> lineasCodigo,
            ResultadoAnalisis resultado) {
        if (lineasCodigo == null) {
            return;
        }

        for (int indice = 0; indice < lineasCodigo.size(); indice++) {
            String linea = lineasCodigo.get(indice);
            Matcher matcher = PATRON_CLASE.matcher(linea);

            if (matcher.matches()) {
                String nombreClase = matcher.group(1);
                int finClase = encontrarFinClase(lineasCodigo, indice);

                ResultadoClase resultadoClase = analizarClase(
                        lineasCodigo,
                        indice,
                        finClase,
                        nombreClase
                );

                resultado.agregarResultadoClase(resultadoClase);
            }
        }
    }

    private ResultadoClase analizarClase(List<String> lineasCodigo, int inicioClase,
            int finClase, String nombreClase) {
        int tamanioClase = 0;
        int numeroMetodos = 0;
        List<String> lineasClase = lineasCodigo.subList(inicioClase, finClase + 1);
        List<String> lineasLogicasClase = normalizarLineasLogicas(lineasClase);

        for (String linea : lineasLogicasClase) {
            tamanioClase += contarLinea(linea);

            if (esDeclaracionMetodo(linea, nombreClase)) {
                numeroMetodos++;
            }
        }

        return new ResultadoClase(nombreClase, numeroMetodos, tamanioClase);
    }

    private int encontrarFinClase(List<String> lineasCodigo, int inicioClase) {
        int balanceLlaves = 0;
        boolean encontroLlaveApertura = false;

        for (int indice = inicioClase; indice < lineasCodigo.size(); indice++) {
            String linea = lineasCodigo.get(indice);

            balanceLlaves += contarLlavesApertura(linea);
            balanceLlaves -= contarLlavesCierre(linea);

            if (contarLlavesApertura(linea) > 0) {
                encontroLlaveApertura = true;
            }

            // El balance en cero indica que termino el bloque de la clase.
            if (encontroLlaveApertura && balanceLlaves == 0) {
                return indice;
            }
        }

        return lineasCodigo.size() - 1;
    }

    private boolean esDeclaracionMetodo(String linea, String nombreClase) {
        if (linea == null) {
            return false;
        }

        String lineaLimpia = linea.trim();

        if (!lineaLimpia.contains("(") || !lineaLimpia.contains(")")) {
            return false;
        }

        if (contienePalabraControl(lineaLimpia)) {
            return false;
        }

        if (lineaLimpia.startsWith("return ") || lineaLimpia.contains("=")) {
            return false;
        }

        int parentesis = lineaLimpia.indexOf('(');
        String antesParentesis = lineaLimpia.substring(0, parentesis).trim();

        if (antesParentesis.contains(".")) {
            return false;
        }

        String[] tokens = antesParentesis.split("\\s+");

        if (tokens.length == 0) {
            return false;
        }

        String nombreMetodo = tokens[tokens.length - 1];

        // El constructor se cuenta como metodo para el reporte PSP.
        if (nombreMetodo.equals(nombreClase)) {
            return true;
        }

        List<String> tokensSinModificadores = quitarModificadores(tokens);
        return tokensSinModificadores.size() >= 2;
    }

    private void analizarVariables(List<String> lineasCodigo,
            ResultadoAnalisis resultado) {
        if (lineasCodigo == null) {
            return;
        }

        List<String> lineasLogicas = normalizarLineasLogicas(lineasCodigo);

        for (String linea : lineasLogicas) {
            List<String> variables = obtenerVariablesInicializadas(linea);

            for (String variable : variables) {
                resultado.agregarVariableInicializada(variable);
            }
        }
    }

    private List<String> obtenerVariablesInicializadas(String linea) {
        List<String> variables = new ArrayList<>();

        if (linea == null) {
            return variables;
        }

        String lineaLimpia = linea.trim();

        if (!lineaLimpia.endsWith(";") || contienePalabraControl(lineaLimpia)) {
            return variables;
        }

        if (lineaLimpia.startsWith("return ") || lineaLimpia.startsWith("throw ")) {
            return variables;
        }

        // Se descartan comparaciones para evitar falsos positivos.
        if (lineaLimpia.contains("==") || lineaLimpia.contains("!=")
                || lineaLimpia.contains(">=") || lineaLimpia.contains("<=")) {
            return variables;
        }

        String sinPuntoComa = lineaLimpia.substring(0, lineaLimpia.length() - 1);
        List<String> declaraciones = separarPorComasPrincipales(sinPuntoComa);

        for (int indice = 0; indice < declaraciones.size(); indice++) {
            String declaracion = declaraciones.get(indice).trim();
            int posicionAsignacion = obtenerPosicionAsignacion(declaracion);

            if (posicionAsignacion < 0) {
                continue;
            }

            String ladoIzquierdo = declaracion.substring(
                    0,
                    posicionAsignacion
            ).trim();

            String nombreVariable;

            if (indice == 0) {
                nombreVariable = obtenerNombreVariablePrimeraDeclaracion(
                        ladoIzquierdo
                );
            } else {
                nombreVariable = obtenerUltimoToken(ladoIzquierdo);
            }

            if (esNombreVariableValido(nombreVariable)) {
                variables.add(nombreVariable);
            }
        }

        return variables;
    }

    private String obtenerNombreVariablePrimeraDeclaracion(String ladoIzquierdo) {
        String sinModificadores = quitarModificadores(ladoIzquierdo);

        if (sinModificadores.contains("(") || sinModificadores.contains(")")
                || sinModificadores.contains(".")) {
            return "";
        }

        String[] tokens = sinModificadores.trim().split("\\s+");

        if (tokens.length < 2) {
            return "";
        }

        return tokens[tokens.length - 1].replace("[]", "").trim();
    }

    private boolean esDeclaracionMultiple(String linea) {
        if (!linea.endsWith(";") || !linea.contains(",")) {
            return false;
        }

        if (contienePalabraControl(linea)) {
            return false;
        }

        List<String> partes = separarPorComasPrincipales(
                linea.substring(0, linea.length() - 1)
        );

        if (partes.size() <= 1) {
            return false;
        }

        String primeraParte = partes.get(0).trim();
        String izquierda = primeraParte;
        int posicionAsignacion = obtenerPosicionAsignacion(primeraParte);

        if (posicionAsignacion >= 0) {
            izquierda = primeraParte.substring(0, posicionAsignacion).trim();
        }

        String sinModificadores = quitarModificadores(izquierda);
        String[] tokens = sinModificadores.split("\\s+");

        return tokens.length >= 2 && esNombreVariableValido(
                tokens[tokens.length - 1]
        );
    }

    private int contarDeclaracionesMultiples(String linea) {
        String sinPuntoComa = linea.substring(0, linea.length() - 1);
        List<String> declaraciones = separarPorComasPrincipales(sinPuntoComa);
        return declaraciones.size();
    }

    private List<String> separarPorComasPrincipales(String texto) {
        List<String> partes = new ArrayList<>();
        StringBuilder actual = new StringBuilder();

        int nivelParentesis = 0;
        int nivelAngulares = 0;
        boolean dentroCadena = false;
        boolean dentroCaracter = false;

        for (int indice = 0; indice < texto.length(); indice++) {
            char caracter = texto.charAt(indice);

            if (caracter == '"' && !dentroCaracter && !estaEscapado(texto, indice)) {
                dentroCadena = !dentroCadena;
            } else if (caracter == '\'' && !dentroCadena
                    && !estaEscapado(texto, indice)) {
                dentroCaracter = !dentroCaracter;
            }

            if (!dentroCadena && !dentroCaracter) {
                switch (caracter) {
                    case '(':
                        nivelParentesis++;
                        break;
                    case ')':
                        nivelParentesis--;
                        break;
                    case '<':
                        nivelAngulares++;
                        break;
                    case '>':
                        nivelAngulares--;
                        break;
                    default:
                        break;
                }

                // Solo se separan comas que no pertenecen a metodos o genericos.
                if (caracter == ',' && nivelParentesis == 0
                        && nivelAngulares == 0) {
                    partes.add(actual.toString());
                    actual.setLength(0);
                    continue;
                }
            }

            actual.append(caracter);
        }

        partes.add(actual.toString());
        return partes;
    }

    private int obtenerPosicionAsignacion(String texto) {
        for (int indice = 0; indice < texto.length(); indice++) {
            char caracter = texto.charAt(indice);
            char anterior = indice > 0 ? texto.charAt(indice - 1) : '\0';
            char siguiente = indice + 1 < texto.length()
                    ? texto.charAt(indice + 1)
                    : '\0';

            if (caracter == '=' && anterior != '=' && anterior != '!'
                    && anterior != '<' && anterior != '>' && siguiente != '=') {
                return indice;
            }
        }

        return -1;
    }

    private int contarLlavesApertura(String linea) {
        return contarCaracterFueraDeTexto(linea, '{');
    }

    private int contarLlavesCierre(String linea) {
        return contarCaracterFueraDeTexto(linea, '}');
    }

    private int contarCaracterFueraDeTexto(String linea, char caracterBuscado) {
        int total = 0;
        boolean dentroCadena = false;
        boolean dentroCaracter = false;

        for (int indice = 0; indice < linea.length(); indice++) {
            char caracter = linea.charAt(indice);

            if (caracter == '"' && !dentroCaracter && !estaEscapado(linea, indice)) {
                dentroCadena = !dentroCadena;
            } else if (caracter == '\'' && !dentroCadena
                    && !estaEscapado(linea, indice)) {
                dentroCaracter = !dentroCaracter;
            }

            if (!dentroCadena && !dentroCaracter && caracter == caracterBuscado) {
                total++;
            }
        }

        return total;
    }

    private boolean esLlaveSola(String linea) {
        return linea.equals("{")
                || linea.equals("}")
                || linea.equals(";")
                || linea.equals("};");
    }

    private boolean contienePalabraControl(String linea) {
        String lineaLimpia = linea.trim();

        for (String palabra : PALABRAS_CONTROL) {
            if (lineaLimpia.startsWith(palabra + " ")
                    || lineaLimpia.startsWith(palabra + "(")) {
                return true;
            }
        }

        return false;
    }

    private List<String> quitarModificadores(String[] tokens) {
        List<String> tokensSinModificadores = new ArrayList<>();

        for (String token : tokens) {
            if (!esModificador(token)) {
                tokensSinModificadores.add(token);
            }
        }

        return tokensSinModificadores;
    }

    private String quitarModificadores(String texto) {
        String resultado = texto.trim();
        boolean huboCambio = true;

        while (huboCambio) {
            huboCambio = false;

            for (String modificador : MODIFICADORES) {
                if (resultado.startsWith(modificador + " ")) {
                    resultado = resultado.substring(modificador.length()).trim();
                    huboCambio = true;
                }
            }
        }

        return resultado;
    }

    private boolean esModificador(String token) {
        for (String modificador : MODIFICADORES) {
            if (modificador.equals(token)) {
                return true;
            }
        }

        return false;
    }

    private String obtenerUltimoToken(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        String[] tokens = texto.trim().split("\\s+");
        return tokens[tokens.length - 1].replace("[]", "").trim();
    }

    private boolean esNombreVariableValido(String texto) {
        return texto != null && texto.matches("[A-Za-z_$][\\w$]*");
    }

    private boolean estaEscapado(String linea, int indice) {
        int contadorBarras = 0;
        int posicion = indice - 1;

        while (posicion >= 0 && linea.charAt(posicion) == '\\') {
            contadorBarras++;
            posicion--;
        }

        return contadorBarras % 2 != 0;
    }

    /**
     * Almacena las metricas completas de un archivo analizado.
     */
    public static class ResultadoAnalisis {

        private final int numeroPrograma;
        private final String nombreArchivo;
        private final int totalLineasFisicas;
        private int totalLOC;
        private final List<ResultadoClase> resultadosClase;
        private final List<String> variablesInicializadas;

        /**
         * Crea un resultado general del analisis.
         *
         * @param numeroPrograma numero de programa
         * @param nombreArchivo nombre del archivo
         * @param totalLineasFisicas total de lineas fisicas
         */
        public ResultadoAnalisis(int numeroPrograma, String nombreArchivo,
                int totalLineasFisicas) {
            this.numeroPrograma = numeroPrograma;
            this.nombreArchivo = nombreArchivo;
            this.totalLineasFisicas = totalLineasFisicas;
            this.resultadosClase = new ArrayList<>();
            this.variablesInicializadas = new ArrayList<>();
        }

        public int getNumeroPrograma() {
            return numeroPrograma;
        }

        public String getNombreArchivo() {
            return nombreArchivo;
        }

        public int getTotalLineasFisicas() {
            return totalLineasFisicas;
        }

        public int getTotalLOC() {
            return totalLOC;
        }

        public void setTotalLOC(int totalLOC) {
            this.totalLOC = totalLOC;
        }

        public List<ResultadoClase> getResultadosClase() {
            return resultadosClase;
        }

        public void agregarResultadoClase(ResultadoClase resultadoClase) {
            resultadosClase.add(resultadoClase);
        }

        public List<String> getVariablesInicializadas() {
            return variablesInicializadas;
        }

        public void agregarVariableInicializada(String variable) {
            variablesInicializadas.add(variable);
        }
    }

    /**
     * Almacena las metricas correspondientes a una clase Java.
     */
    public static class ResultadoClase {

        private final String nombreClase;
        private final int numeroMetodos;
        private final int tamanioClase;

        /**
         * Crea un resultado de clase.
         *
         * @param nombreClase nombre de la clase
         * @param numeroMetodos metodos detectados
         * @param tamanioClase tamano de clase en LOC
         */
        public ResultadoClase(String nombreClase, int numeroMetodos,
                int tamanioClase) {
            this.nombreClase = nombreClase;
            this.numeroMetodos = numeroMetodos;
            this.tamanioClase = tamanioClase;
        }

        public String getNombreClase() {
            return nombreClase;
        }

        public int getNumeroMetodos() {
            return numeroMetodos;
        }

        public int getTamanioClase() {
            return tamanioClase;
        }
    }
}