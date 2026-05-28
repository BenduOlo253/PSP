/**
 * Almacena las metricas calculadas para una clase Java analizada.
 * Forma parte del Programa 1 de PSP0.1 para conteo de LOC.
 */
public class ResultadoClase {
    private final String nombreClase;
    private final int lineaInicio;
    private int lineaFin;
    private int numeroMetodos;
    private int tamanoLoc;

    /**
     * Crea un resultado de clase con su nombre y linea inicial.
     *
     * @param nombreClase nombre de la clase detectada
     * @param lineaInicio linea fisica donde inicia la declaracion
     */
    public ResultadoClase(String nombreClase, int lineaInicio) {
        this.nombreClase = nombreClase;
        this.lineaInicio = lineaInicio;
        this.lineaFin = lineaInicio;
        this.numeroMetodos = 0;
        this.tamanoLoc = 0;
    }

    /**
     * Obtiene el nombre de la clase.
     *
     * @return nombre de la clase
     */
    public String obtenerNombreClase() {
        return nombreClase;
    }

    /**
     * Obtiene la linea inicial de la clase.
     *
     * @return linea inicial fisica
     */
    public int obtenerLineaInicio() {
        return lineaInicio;
    }

    /**
     * Obtiene la linea final de la clase.
     *
     * @return linea final fisica
     */
    public int obtenerLineaFin() {
        return lineaFin;
    }

    /**
     * Asigna la linea final de la clase.
     *
     * @param lineaFin linea fisica final
     */
    public void asignarLineaFin(int lineaFin) {
        this.lineaFin = lineaFin;
    }

    /**
     * Obtiene el numero de metodos de la clase.
     *
     * @return numero de metodos y constructores
     */
    public int obtenerNumeroMetodos() {
        return numeroMetodos;
    }

    /**
     * Incrementa en uno el numero de metodos detectados.
     */
    public void incrementarNumeroMetodos() {
        numeroMetodos++;
    }

    /**
     * Obtiene el tamano de la clase en LOC.
     *
     * @return LOC de la clase
     */
    public int obtenerTamanoLoc() {
        return tamanoLoc;
    }

    /**
     * Asigna el tamano de la clase en LOC.
     *
     * @param tamanoLoc LOC calculadas para la clase
     */
    public void asignarTamanoLoc(int tamanoLoc) {
        this.tamanoLoc = tamanoLoc;
    }
}
