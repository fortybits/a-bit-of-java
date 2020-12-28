package edu.bit.__dump;

public class ConsA {

    public ConsA() {
        System.out.println("A");
        h();
    }

    public void h() {
        String className = this.getClass().getName();
        System.out.println(className);
    }
}