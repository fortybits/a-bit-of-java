package edu.bit;

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

}