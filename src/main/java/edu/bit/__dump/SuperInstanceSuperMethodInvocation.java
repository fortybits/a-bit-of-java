package edu.bit.__dump;

public class SuperInstanceSuperMethodInvocation {

    public static void main(String[] args) {
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