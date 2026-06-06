public class PruebaComentarios {

    public void probar() {
        int a = 1, b = 2;
        String texto = "Esto no es comentario // dentro de cadena";
        String bloque = "Tampoco es comentario /* dentro de cadena */";

        // Esta linea no debe contarse

        /*
         Este bloque tampoco debe contarse
        */

        System.out.println(texto);
        System.out.println(bloque);
    }
}