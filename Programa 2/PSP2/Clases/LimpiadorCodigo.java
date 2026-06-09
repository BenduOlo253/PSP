/**
 * Nombre del archivo: LimpiadorCodigo.java
 * Autor: Jose Manuel Roberto Badillo
 * Fecha de creacion: 31/05/2026
 * Version: 1.0
 * Descripcion: Limpia comentarios y elementos no contables del codigo fuente.
 */

package Clases;

import java.util.ArrayList;
import java.util.List;

public class LimpiadorCodigo {

    /**
     * Elimina comentarios de linea, comentarios de bloque y JavaDoc.
     *
     * @param lineasOriginales lineas originales del archivo
     * @return lineas limpias para el conteo
     */
    public List<String> limpiarCodigo(List<String> lineasOriginales) {
        List<String> lineasLimpias = new ArrayList<>();

        if (lineasOriginales == null) {
            return lineasLimpias;
        }

        boolean dentroComentarioBloque = false;

        for (String lineaOriginal : lineasOriginales) {
            StringBuilder lineaLimpia = new StringBuilder();
            boolean dentroCadena = false;
            boolean dentroCaracter = false;

            for (int indice = 0; indice < lineaOriginal.length(); indice++) {
                char caracterActual = lineaOriginal.charAt(indice);
                char siguienteCaracter = obtenerSiguienteCaracter(lineaOriginal, indice);

                if (dentroComentarioBloque) {
                    if (caracterActual == '*' && siguienteCaracter == '/') {
                        dentroComentarioBloque = false;
                        indice++;
                    }

                    continue;
                }

                // Los comentarios dentro de cadenas no deben eliminarse.
                if (!dentroCadena && !dentroCaracter && caracterActual == '/'
                        && siguienteCaracter == '/') {
                    break;
                }

                if (!dentroCadena && !dentroCaracter && caracterActual == '/'
                        && siguienteCaracter == '*') {
                    dentroComentarioBloque = true;
                    indice++;
                    continue;
                }

                if (caracterActual == '"' && !dentroCaracter
                        && !estaEscapado(lineaOriginal, indice)) {
                    dentroCadena = !dentroCadena;
                }

                if (caracterActual == '\'' && !dentroCadena
                        && !estaEscapado(lineaOriginal, indice)) {
                    dentroCaracter = !dentroCaracter;
                }

                lineaLimpia.append(caracterActual);
            }

            lineasLimpias.add(lineaLimpia.toString());
        }

        return lineasLimpias;
    }

    private char obtenerSiguienteCaracter(String linea, int indice) {
        if (indice + 1 >= linea.length()) {
            return '\0';
        }

        return linea.charAt(indice + 1);
    }

    private boolean estaEscapado(String linea, int indice) {
        int cantidadBarrasInvertidas = 0;
        int posicion = indice - 1;

        while (posicion >= 0 && linea.charAt(posicion) == '\\') {
            cantidadBarrasInvertidas++;
            posicion--;
        }

        // Un numero impar de diagonales indica que el caracter esta escapado.
        return cantidadBarrasInvertidas % 2 != 0;
    }
}