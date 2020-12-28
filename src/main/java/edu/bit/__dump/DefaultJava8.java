package edu.bit.__dump;

public class DefaultJava8 {

    public static void main(String[] args) {
        new DefaultJava8().use();
        System.out.println("Hey!");
    }

    public void use() {
        SeaPlane seaPlane = new SeaPlane();
        seaPlane.takeOff();
        seaPlane.turn();
        seaPlane.cruise();
        seaPlane.land(); // Method in class hierarchy rules

    }

}