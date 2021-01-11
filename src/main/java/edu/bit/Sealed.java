package edu.bit;

import java.io.Serializable;

public class Sealed {

    public void sealedExperiments() {
        Integral integral = new Integral();
        integral.bar();
        integral.foo();
    }

    public sealed interface Int permits Some {
        void foo();

        void bar();
    }

    static abstract sealed class Some implements Int permits Integral {
        public void foo() {
            System.out.println("sealed some");
        }
    }

    static non-sealed class Integral extends Some {

        @Override
        public void bar() {
            System.out.println("bar from non-sealed class");
        }

        @Override
        public void foo() {
            System.out.println("foo from non-sealed class");
        }

    }

    public sealed class A permits B {
    }

    public final class B extends A implements Serializable {
    }
}