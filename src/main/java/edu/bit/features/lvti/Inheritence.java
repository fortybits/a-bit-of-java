package edu.bit.features.lvti;

public class Inheritence {

    public static void main(String[] args) {
        var myA = new A();
        myA.someMethod();
        myA = ((A) new B());
        myA.someMethod();
    }

    public static class A {
        public void someMethod() {
            System.out.println("A!");
        }
    }

    public static class B extends A {
        @Override
        public void someMethod() {
            System.out.println("B that extends A!");
        }
    }
}