package Clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EscritorArchivos {

    public void Escribir() throws Exception {
        System.out.println("\nIngrese el titulo del archivo de datos, recuerde que no deben sobrepasar los 14 pares y que el formato debe ser:\na b\nb c\n...");
        Scanner entradaDatos = new Scanner(System.in);
        String tituloArchivo = entradaDatos.nextLine();
        
        try {
            FileWriter ArchivoCreado = new FileWriter(tituloArchivo + ".txt", true);
            BufferedWriter escritorArchivo = new BufferedWriter(ArchivoCreado);
            int contador = 0;
            int centinela = 0;
            String linea;

            while (contador < 15 && centinela == 0) {
                contador++;
                while (true) {
                    System.out.println("\nIngrese el par no." + contador + " de datos, recuerde que solo se aceptan numeros reales, separados por un espacio. Ingrese FIN para terminar.\n");
                    linea = entradaDatos.nextLine();

                    if (linea.equalsIgnoreCase("FIN")) {
                        centinela = 1;
                        break;
                    } else if (this.VerificarLinea(linea)) {
                        escritorArchivo.write(linea + "\n");
                        escritorArchivo.flush();
                        break;
                    }
                }
            }
            escritorArchivo.close();
        } catch (IOException e) {
            throw new Exception("Error al escribir/guardar el archivo:\n");
        }
    }

    public boolean VerificarLinea(String linea) throws Exception {
        try {
            String[] lineaVerificar = linea.split("\\s+");
            if (lineaVerificar.length != 2){
                System.out.println("\nLa linea que usted escribio no es valida.\n");
                return false;
            }
            
            Double.parseDouble(lineaVerificar[0]);
            Double.parseDouble(lineaVerificar[1]);
            return true;
        } catch (Exception e) {
            System.out.println("La linea que usted escribio no es valida..\n");
            return false;
        }
    }
}