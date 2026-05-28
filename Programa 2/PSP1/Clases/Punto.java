package Clases;

public class Punto{

    private double x;
    private double y;
    
    public Punto(double valorAsignadoX, double valorAsignadoY){
        this.x = valorAsignadoX;
        this.y = valorAsignadoY;
    }
    
        public double getValorX(){
            return this.x;
        }
    
        public double getValorY(){
            return this.y;
        }

}