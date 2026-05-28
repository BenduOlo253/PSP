import java.util.Scanner;
import Clases.EscritorArchivos;
import Clases.LectorArchivo;
import Clases.Punto;
import Clases.Calculadora;
import java.util.ArrayList;

public class App{
    public static void main(String[] args) {
        int centinela = 0;
        while(centinela == 0){
            Scanner entradaDatos = new Scanner(System.in);
            System.out.println("--- MENÚ DE ARCHIVOS ---");
            System.out.println("1. Escribir un archivo");
            System.out.println("2. Leer un archivo");
    
            String opcion = entradaDatos.nextLine().trim();
    
    
            try {
                if (opcion.equals("1")) {
                    EscritorArchivos escritor = new EscritorArchivos();
                    escritor.Escribir();
                } else if (opcion.equals("2")) {
                    LectorArchivo lector = new LectorArchivo();
                    ArrayList<Punto> Puntos = lector.Verificar();
                    lector.MostrarContenido(Puntos);
                    System.out.println("\nIngrese 1 para calcular la media y la desviacion estandar y 2 para salir.");
                    opcion = entradaDatos.nextLine().trim();
                    if(opcion.equals("1")){
                        Calculadora calculadora = new Calculadora();
                        System.out.println(calculadora.Calcular(Puntos));
                    }else if (opcion.equals("2")){
                        System.out.println("\nHasta pronto!");
                        centinela = 1;
                    }else{
                        System.out.println("\nLa opcion que usted ha ingresado no es valida");
                    }
                } else{
                    System.out.println("\nLa opcion que usted ha elegido no existe.");
                }
            } catch (Exception e) {
                System.out.println("\n[!] ¡Ups! Algo salió mal:");
                System.out.println(">>> " + e.getMessage()); 
                System.out.println("\nPor favor, verifica los datos e intenta de nuevo.");
            }
        }
    }
}