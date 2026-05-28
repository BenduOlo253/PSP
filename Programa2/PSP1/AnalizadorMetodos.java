/**
 * Archivo de soporte para detectar metodos del Programa 1 PSP0.1.
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detecta declaraciones de metodos y constructores Java por analisis lexico.
 * Excluye estructuras de control para evitar falsos positivos.
 */
public class AnalizadorMetodos {
    private static final Pattern PATRON_NOMBRE_PRE_PARENTESIS = Pattern.compile(
            "([A-Za-z_$][\\w$]*)\\s*\\([^;]*\\)\\s*(?:throws\\s+[^{};]+)?\\s*(?:\\{|;)?\\s*$");
    private static final Set<String> PALABRAS_CONTROL = new HashSet<String>(Arrays.asList(
            "if", "for", "while", "switch", "catch", "try", "do", "else",
            "return", "throw", "new", "case"));

    /**
     * Indica si una linea limpia contiene una declaracion de metodo o constructor.
     *
     * @param linea linea sin comentarios
     * @param nombreClaseActual nombre de la clase donde se analiza
     * @return true si la linea declara un metodo o constructor
     */
    public boolean esDeclaracionMetodo(String linea, String nombreClaseActual) {
        String normalizada = linea.trim();
        if (!normalizada.contains("(") || normalizada.startsWith("//")) {
            return false;
        }
        if (normalizada.endsWith(",") || normalizada.startsWith("@")
                || normalizada.startsWith("synchronized (")
                || normalizada.startsWith("synchronized(")) {
            return false;
        }
        String primeraPalabra = obtenerPrimeraPalabra(normalizada);
        if (PALABRAS_CONTROL.contains(primeraPalabra)) {
            return false;
        }
        if (normalizada.contains(" class ") || normalizada.startsWith("class ")) {
            return false;
        }
        Matcher matcher = PATRON_NOMBRE_PRE_PARENTESIS.matcher(normalizada);
        if (!matcher.find()) {
            return false;
        }
        String nombreMetodo = matcher.group(1);
        if (PALABRAS_CONTROL.contains(nombreMetodo)) {
            return false;
        }
        if (nombreMetodo.equals(nombreClaseActual)) {
            return true;
        }
        String antesDelNombre = normalizada.substring(0, matcher.start(1)).trim();
        return !antesDelNombre.isEmpty() && contieneTipoRetorno(antesDelNombre);
    }

    private String obtenerPrimeraPalabra(String linea) {
        Matcher matcher = Pattern.compile("^([A-Za-z_$][\\w$]*)").matcher(linea);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private boolean contieneTipoRetorno(String prefijo) {
        String normalizado = prefijo.replaceAll("<[^>]+>", "Generico");
        String[] partes = normalizado.split("\\s+");
        return partes.length >= 2;
    }
}
