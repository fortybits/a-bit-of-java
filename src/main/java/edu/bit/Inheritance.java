package edu.bit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Objects;

public class Inheritance {

    // basic inheritance of classes and their instance creation
    public abstract static class Animal {
        private final String kind;
        private final String appearance;

        Animal(String Kind, String Appearance) {
            this.kind = Kind;
            this.appearance = Appearance;
        }

        public abstract void eat();

        public abstract void move();

        @Override
        public String toString() {
            return "(" + kind + "," + appearance + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Animal animal = (Animal) o;
            if (!Objects.equals(kind, animal.kind)) {
                return false;
            }
            return Objects.equals(appearance, animal.appearance);
        }

        @Override
        public int hashCode() {
            int result = kind != null ? kind.hashCode() : 0;
            result = 31 * result + (appearance != null ? appearance.hashCode() : 0);
            return result;
        }
    }

    public abstract static class Bird extends Animal {
        Bird(String kind, String appearance) {
            super(kind, appearance);
        }

        @Override
        public void eat() {
            System.out.println("Eats seeds and insects");
        }

        @Override
        public void move() {
            System.out.println("Flies throught the air");
        }
    }

    public static class AmericanRobin extends Bird {

        String fly;

        AmericanRobin(String kind, String appearance) {
            super(kind, appearance);
        }

        public String getFly() {
            return fly;
        }

        public void setFly(String fly) {
            this.fly = fly;
        }

    }

    // default methods invoked from parent interfaces via their extensions
    public class Invoice {
    }

    public interface Another<T> {

        default T someMethod() {
            return null;
        }
    }

    public interface DefaultFromParentInterface extends Another<Invoice> {
        // some other methods here
        default Invoice save(Invoice invoice) {
            return Another.super.someMethod();
        }
    }


    /**
     * Default methods get inherited automatically
     * We can override a default method
     * Method in class hierarchy rules
     * If there is a collision - hierarchy needs to be resolved
     */
    interface Fly {
        default void takeOff() {
            System.out.println("Fly :: takeOff");
        }

        default void turn() {
            System.out.println("Fly :: turn");
        }

        default void cruise() {
            System.out.println("Fly :: cruise");
        }

        default void land() {
            System.out.println("Fly :: land");
        }
    }


    static class SeaPlane extends Vehicle implements FastFly, Sail {
        public void cruise() {
            System.out.println("SeaPlane :: cruise");
            FastFly.super.cruise(); // super used for default method
        }
    }

    interface Sail {
        default void cruise() {
            System.out.println("Sail :: cruise");
        }
    }

    interface FastFly extends Fly {
        default void takeOff() {
            System.out.println("FastFly :: takeOff");  // We can override a default method
        }
    }

    static class Vehicle {
        public void land() {
            System.out.println("Vehicle :: land");
        }
    }

    public static void defaultMethodBehaviourInJava8(String[] args) {
        new Inheritance().useDefaultMethodsInSeaPlane();
        System.out.println("Hey!");
    }

    public void useDefaultMethodsInSeaPlane() {
        SeaPlane seaPlane = new SeaPlane();
        seaPlane.takeOff();
        seaPlane.turn();
        seaPlane.cruise();
        seaPlane.land(); // Method in class hierarchy rules
    }

    // interface implemented by enum
    enum H implements G {
        ONE, TWO;

        @Override
        public void s() {
        }
    }

    interface G {
        void s();
    }

    // typical examples of inheritance with shapes
    public abstract static class Shape implements Serializable {
        private static final long serialVersionUID = -1231855623100981927L;

        public abstract boolean draw();

        public abstract String area();

        public abstract String perimeter();

        public abstract String characteristic();
    }

    public static class Rectangle extends Shape {

        private double x;
        private double y;

        public Rectangle() {
        }

        public Rectangle(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        @Override
        public boolean draw() {
            boolean pass = false;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter X:");
                String xvalue = br.readLine();
                x = Double.parseDouble(xvalue);
                System.out.print("Enter Y:");
                String yvalue = br.readLine();
                y = Double.parseDouble(yvalue);
                if (x == y) {
                    System.out.println("Square detected");
                    Rectangle r = new Square(x);
                    // I want to return Square not Rectangle
                }
                System.err.println("Enter succesful");
                pass = true;
            } catch (IOException ex) {
                pass = false;
                ex.printStackTrace();
            } catch (NumberFormatException e) {
                pass = false;
                System.err.println("Enter error. Please enter right format, go to 6. Help for more details");
            }
            return pass;
        }

        @Override
        public String area() {
            double area = x * y;
            return "I'm a rectangle, I have 4 right angles. My sides are " + x + ", " + y + ", " + x + ", " + y +
                    " My area is " + area;
        }

        @Override
        public String perimeter() {
            double per = (x + y) * 2;
            return "I'm a rectangle, I have 4 right angles. My sides are " + x + ", " + y + ", " + x + ", " + y +
                    " My perimeter is " + per;
        }

        @Override
        public String characteristic() {
            return "I'm a rectangle, I have 4 right angles. My sides are " + x + ", " + y + ", " + x + ", " + y;
        }
    }

    public static class Square extends Rectangle {

        private double x;

        public Square() {
        }


        public Square(double side) {
            super(side, side);
            this.x = side;
        }

        public Square square(double side) {
            this.x = side;
            return this;
        }


        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        @Override
        public boolean draw() {
            boolean pass = false;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter X:");
                String xvalue = br.readLine();
                x = Double.parseDouble(xvalue);
                System.err.println("Enter succesful");
                pass = true;
            } catch (IOException ex) {
                pass = false;
                ex.printStackTrace();
            } catch (NumberFormatException e) {
                pass = false;
                System.err.println("Please enter right format, go to 6. Help for more details");
            }
            return pass;
        }

        @Override
        public String area() {
            double area = x * x;
            return "I'm a square, I have 4 right angles. My sides are " + x + ", " + x + ", " + x + ", " + x +
                    ". My area is " + area;
        }

        @Override
        public String perimeter() {
            double per = x * 4;
            return "I'm a square, I have 4 right angles. My sides are " + x + ", " + x + ", " + x + ", " + x +
                    ". My perimeter is " + per;
        }

        @Override
        public String characteristic() {
            return "I'm a square, I have 4 right angles. My sides are " + x + ", " + x + ", " + x + ", " + x;
        }
    }


    //
    public interface SubInt extends SuperInt {
        @Override
        default void method3() {  //  compile time error
            System.out.println("Inside SubInt");
        }
    }

    public static class SubIntImpl implements SubInt {
        @Override
        public void method4() {

        }

        @Override
        public void method3() {
        }
    }

    public interface SuperInt {

        void method3();   // completely qualified

        void method4(); // I am the boss of this class
    }

    public static class SuperIntImpl implements SuperInt {

        @Override
        public void method3() {
            // must have my own implementation
        }

        @Override
        public void method4() {
            // must have my own implementation
        }
    }

    //
    public void superInstanceSuperMethodInvocation() {
        Jedi jedi = new Luke();
        jedi.attack();
    }

    static class Jedi {
        static void attack() {
            System.out.println("Jedi's attack.");
        }
    }

    static class Luke extends Jedi {
        static void attack() {
            System.out.println("Luke's attack.");
        }
    }
}