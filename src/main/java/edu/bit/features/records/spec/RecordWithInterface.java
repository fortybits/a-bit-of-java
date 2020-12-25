package edu.bit.features.records.spec;

public class RecordWithInterface {

    public static void main(String[] args) {
        new Summable("message").method();
    }

    interface Able {
        default void method() {
            System.out.println("interface able");
        }
    }

    record Summable(String msg) implements Able {
        @Override
        public void method() {
            System.out.println("record able");
        }
    }
}
