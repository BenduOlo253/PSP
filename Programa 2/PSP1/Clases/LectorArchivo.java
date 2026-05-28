package Clases;

import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorArchivo{

    Scanner entradaDatos = new Scanner(System.in);
    
    public ArrayList<Punto> Verificar() throws Exception {
        System.out.println("Ingrese la ruta del archivo.txt:\n");
        String rutaArchivo = entradaDatos.nextLine();
        
        ArrayList<Punto> Puntos = new ArrayList<>();

        try(BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo))){        
            String[] puntoNoProcesado;
            String linea = lector.readLine();
            while(linea != null){
                puntoNoProcesado = linea.split("\\s+");
                if(puntoNoProcesado.length != 2){
                    throw new Exception("El Formato del archivo no es el correcto: \nLinea no correcta: " + " " + linea);
                }
                try{
                    
                    double x = Double.parseDouble(puntoNoProcesado[0]);
                    double y = Double.parseDouble(puntoNoProcesado[1]);
                    Punto puntoProcesado = new Punto(x, y);
                    Puntos.add(puntoProcesado);
                }catch(NumberFormatException e){
                    throw new Exception("El archivo no contiene valores numericos reales, verifique el formato de su archivo.\n");
                }
                linea = lector.readLine();
            }
        }catch(IOException e){
            throw new Exception("Error al leer el archivo, verifique que la ruta sea la correcta y que el archivo sea .txt" );
        }
        return Puntos;
    }
    
    public void MostrarContenido(ArrayList<Punto> puntos){
        System.out.println("Columna 1        Columna2\n");
        for(Punto punto : puntos){
            System.out.println(punto.getValorX() + "        " + punto.getValorY());
        }
    
    }
}