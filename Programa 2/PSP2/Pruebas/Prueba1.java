
/**
 * Clase de prueba para demostrar la estructura basica de una clase en Java.
 * Contiene un constructor, un metodo para saludar y una clase auxiliar para sumar dos numeros.
 */

public class Prueba1 {

    private int edad = 20;
    private String nombre;

    public Prueba1() {
        nombre = "Jose";
    }

    public void saludar() {
        System.out.println(nombre);
    }
}

class Auxiliar {

    public String sumar(int a, int b) {
        return " La suma es: " + (a + b);
    }
}
