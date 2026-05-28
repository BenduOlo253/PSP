/**
 * Archivo de soporte para almacenar resultados del Programa 1 PSP0.1.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contiene las metricas calculadas para un archivo fuente Java.
 * Forma parte del Programa 1 de PSP0.1 para conteo de LOC.
 */
public class ResultadoPrograma {
    private final int numeroPrograma;
    private final String nombreArchivo;
    private final int totalLineasFisicas;
    private final int locTotal;
    private final List<ResultadoClase> resultadosClase;
    private final List<String> variablesInicializadas;

    /**
     * Crea un resultado de programa con sus metricas principales.
     *
     * @param numeroPrograma numero ordinal del archivo procesado
     * @param nombreArchivo nombre del archivo fuente
     * @param totalLineasFisicas total de lineas fisicas leidas
     * @param locTotal total de LOC logicas calculadas
     * @param resultadosClase resultados por clase
     * @param variablesInicializadas variables declaradas e inicializadas
     */
    public ResultadoPrograma(int numeroPrograma, String nombreArchivo,
            int totalLineasFisicas, int locTotal,
            List<ResultadoClase> resultadosClase,
            List<String> variablesInicializadas) {
        this.numeroPrograma = numeroPrograma;
        this.nombreArchivo = nombreArchivo;
        this.totalLineasFisicas = totalLineasFisicas;
        this.locTotal = locTotal;
        this.resultadosClase = new ArrayList<ResultadoClase>(resultadosClase);
        this.variablesInicializadas = new ArrayList<String>(variablesInicializadas);
    }

    /**
     * Obtiene el numero del programa.
     *
     * @return numero ordinal del programa
     */
    public int obtenerNumeroPrograma() {
        return numeroPrograma;
    }

    /**
     * Obtiene el nombre del archivo.
     *
     * @return nombre del archivo fuente
     */
    public String obtenerNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * Obtiene el total de lineas fisicas.
     *
     * @return total de lineas fisicas
     */
    public int obtenerTotalLineasFisicas() {
        return totalLineasFisicas;
    }

    /**
     * Obtiene el total de LOC logicas.
     *
     * @return total de LOC logicas
     */
    public int obtenerLocTotal() {
        return locTotal;
    }

    /**
     * Obtiene el total de clases detectadas.
     *
     * @return numero total de clases
     */
    public int obtenerTotalClases() {
        return resultadosClase.size();
    }

    /**
     * Obtiene los resultados por clase.
     *
     * @return lista no modificable de resultados por clase
     */
    public List<ResultadoClase> obtenerResultadosClase() {
        return Collections.unmodifiableList(resultadosClase);
    }

    /**
     * Obtiene los mensajes de variables inicializadas.
     *
     * @return lista no modificable de mensajes
     */
    public List<String> obtenerVariablesInicializadas() {
        return Collections.unmodifiableList(variablesInicializadas);
    }
}
