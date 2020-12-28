package edu.bit.__dump;

class ImpFunctInterface {
    public static void main(String[] args) {
        new ImpFunctInterface().foo(System.out::println);
    }

    void foo(FunctionalInterface f) {
        ImpFunctInterface a = new ImpFunctInterface();
        f.activate(a);
    }

    private interface FunctionalInterface {
        void activate(ImpFunctInterface a);
    }
}