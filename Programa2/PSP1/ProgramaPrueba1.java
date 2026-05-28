/**
 * Archivo de prueba uno para validar el analizador LOC PSP0.1.
 * Incluye comentarios, clases multiples y declaraciones de variables.
 */
class ProgramaPrueba1 {
    private int contador = 0;

    /**
     * Ejecuta operaciones simples de prueba.
     */
    public void ejecutar() {
        int x = 1, y = 2;
        String nombre = "Ana // no es comentario";
        contador = x + y;
        System.out.println(nombre + contador);
    }

    /* Comentario de bloque que no debe contar.
       int falso = 100;
     */
    public int sumar(int primero, int segundo) {
        return primero + segundo;
    }
}

/**
 * Segunda clase del primer programa de prueba.
 */
class AuxiliarPrueba1 {
    private String etiqueta = "Inicial";

    public AuxiliarPrueba1() {
        etiqueta = "Creada";
    }

    public void mostrar() {
        System.out.println(etiqueta);
    }
}
