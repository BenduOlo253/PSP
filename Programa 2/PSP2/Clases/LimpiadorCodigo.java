package Clases;

import java.util.ArrayList;
import java.util.List;

/**
 * Nombre del archivo: LimpiadorCodigo.java
 * Autor: Jose Manuel Roberto Badillo
 * Fecha de creación: 31/05/2026
 * Versión: 1.0
 * Descripción: Limpia comentarios y elementos no contables del código fuente.
 */
public class LimpiadorCodigo {

    /**
     * Elimina comentarios de línea, comentarios de bloque y JavaDoc.
     *
     * @param lineasOriginales líneas originales del archivo
     * @return líneas limpias para el conteo
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
                char actual = lineaOriginal.charAt(indice);
                char siguiente = obtenerSiguienteCaracter(lineaOriginal, indice);

                if (dentroComentarioBloque) {
                    if (actual == '*' && siguiente == '/') {
                        dentroComentarioBloque = false;
                        indice++;
                    }

                    continue;
                }

                // Los comentarios dentro de cadenas no deben eliminarse.
                if (!dentroCadena && !dentroCaracter && actual == '/'
                        && siguiente == '/') {
                    break;
                }

                if (!dentroCadena && !dentroCaracter && actual == '/'
                        && siguiente == '*') {
                    dentroComentarioBloque = true;
                    indice++;
                    continue;
                }

                if (actual == '"' && !dentroCaracter
                        && !estaEscapado(lineaOriginal, indice)) {
                    dentroCadena = !dentroCadena;
                }

                if (actual == '\'' && !dentroCadena
                        && !estaEscapado(lineaOriginal, indice)) {
                    dentroCaracter = !dentroCaracter;
                }

                lineaLimpia.append(actual);
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
        int contadorBarras = 0;
        int posicion = indice - 1;

        while (posicion >= 0 && linea.charAt(posicion) == '\\') {
            contadorBarras++;
            posicion--;
        }

        // Un número impar de diagonales indica que el carácter está escapado.
        return contadorBarras % 2 != 0;
    }
}