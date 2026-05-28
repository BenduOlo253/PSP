package Clases;

import java.util.ArrayList;

public class Calculadora{
       
    public String Calcular(ArrayList<Punto> Puntos){
        double sumaColumna1 = 0.0;
        double sumaColumna2 = 0.0;
        for (Punto punto : Puntos){
            sumaColumna1+= punto.getValorX();
            sumaColumna2+= punto.getValorY();
        }
        double mediaColumna1 = sumaColumna1/Puntos.size();
        double mediaColumna2 = sumaColumna2/Puntos.size();
        
        double sumaDiferenciasColumna1 = 0.0;
        double sumaDiferenciasColumna2 = 0.0;
        
        for(Punto punto : Puntos){
            sumaDiferenciasColumna1 += Math.pow(punto.getValorX() - mediaColumna1, 2);
            sumaDiferenciasColumna2 += Math.pow(punto.getValorY() - mediaColumna2, 2);
        }
        double varianzaColumna1 = sumaDiferenciasColumna1/(Puntos.size()-1);
        double varianzaColumna2 = sumaDiferenciasColumna2/(Puntos.size()-1);
        double DMColumna1 = Math.sqrt(varianzaColumna1);
        double DMColumna2 = Math.sqrt(varianzaColumna2);
        
        return String.format("                    Media            Desviacion Estandar        \nTabla 1: Columna 1    %.6f            %.7f \nTabla: Columna2    %.6f            %.7f",mediaColumna1, DMColumna1, mediaColumna2, DMColumna2);
    }
}