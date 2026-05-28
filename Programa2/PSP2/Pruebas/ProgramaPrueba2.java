/**
 * Archivo de prueba dos para el Programa 1 PSP0.1.
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Archivo de prueba dos para validar constructores, imports y estructuras.
 */
class ProgramaPrueba2 {
    private final List<String> datos = new ArrayList<String>();

    /**
     * Constructor con declaracion inicializada y asignacion normal.
     */
    public ProgramaPrueba2(String valorInicial) {
        String valor = valorInicial;
        datos.add(valor);
        valor = "cambio";
    }

    /**
     * Recorre los datos y usa estructuras de control.
     */
    public void procesar() {
        for (int indice = 0; indice < datos.size(); indice++) {
            if (datos.get(indice).equals("fin")) {
                break;
            }
            try {
                System.out.println(datos.get(indice));
            } catch (IllegalArgumentException excepcion) {
                continue;
            }
        }
    }
}
